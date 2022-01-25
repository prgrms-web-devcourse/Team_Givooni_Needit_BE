package com.prgrms.needit.domain.user.service;

import com.prgrms.needit.common.domain.service.UploadService;
import com.prgrms.needit.common.enums.UserType;
import com.prgrms.needit.common.error.ErrorCode;
import com.prgrms.needit.common.error.exception.NotFoundResourceException;
import com.prgrms.needit.domain.center.entity.Center;
import com.prgrms.needit.domain.center.repository.CenterRepository;
import com.prgrms.needit.domain.favorite.entity.FavoriteCenter;
import com.prgrms.needit.domain.favorite.repository.FavoriteCenterRepository;
import com.prgrms.needit.domain.member.entity.Member;
import com.prgrms.needit.domain.member.repository.MemberRepository;
import com.prgrms.needit.domain.user.dto.CurUser;
import com.prgrms.needit.domain.user.dto.Request;
import com.prgrms.needit.domain.user.dto.Response;
import com.prgrms.needit.domain.user.entity.CenterSideInfo;
import com.prgrms.needit.domain.user.entity.Users;
import com.prgrms.needit.domain.user.repository.CenterSideInfoRepository;
import com.prgrms.needit.domain.user.repository.UserRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserService {

	private static final String DIRNAME = "user";
	private static final String DEFAULT_FILE_URL = "https://d2lwizg8138gm8.cloudfront.net/img/wikitree/210719/25d7df1b39bce7575f39c0506d9f24af.jpg";
	private final UserRepository userRepository;
	private final CenterSideInfoRepository centerSideInfoRepository;
	private final FavoriteCenterRepository favoriteCenterRepository;
	private final PasswordEncoder passwordEncoder;
	private final MemberRepository memberRepository;
	private final CenterRepository centerRepository;
	private final AuthService authService;
	private final UploadService uploadService;

	@Transactional(readOnly = true)
	public Response.UserInfo getUserInfo() {
		Users curUser = authService.getCurUser();

		List<Response.Center> myFavorite = new ArrayList<>();

		if (curUser.getUserRole()
				   .equals(UserType.MEMBER)) {
			myFavorite = favoriteCenterRepository.findAllByMemberOrderByCreatedAt(curUser)
												 .stream()
												 .map(FavoriteCenter::getCenter)
												 .map(center ->
														  new Response.Center(
															  center.getId(),
															  center.getNickname(),
															  center.getImage()
														  ))
												 .collect(Collectors.toList());
		}

		// TODO : 작성 게시글은 기부글/기부요청글의 작성자와의 연관관계도 변경해야하기 때문에 그 후 진행

		return new Response.UserInfo(
			CurUser.toResponse(curUser, curUser.getCenterMoreInfo()), null, myFavorite);
	}

	@Transactional(readOnly = true)
	public Response.UserInfo getOtherUser(Long id) {
		Users user = userRepository.findByIdAndIsDeletedFalse(id)
								   .orElseThrow(() -> new NotFoundResourceException(
									   ErrorCode.NOT_FOUND_USER)
								   );

		return new Response.UserInfo(
			CurUser.toResponse(user, user.getCenterMoreInfo()), null, null);
	}

	@Transactional(readOnly = true)
	public List<CurUser> searchCenter(String centerName) {
		return userRepository.findAllByNicknameContainingAndUserRoleAndIsDeletedFalse(
								 centerName, UserType.CENTER)
							 .stream()
							 .map(users -> CurUser.toResponse(users, users.getCenterMoreInfo()))
							 .collect(Collectors.toList());
	}

	@Transactional
	public Long signUp(Request.Profile request) {
		CenterSideInfo centerSideInfo = null;

		if (request.getRole()
				   .equals(UserType.CENTER.getType())) {

			centerSideInfo = centerSideInfoRepository.save(
				CenterSideInfo.addCenterSideInfo(
					request.getOwner(), request.getRegistrationCode()
				));
		}

		return userRepository.save(Users.builder()
										.nickname(request.getNickname())
										.email(request.getEmail())
										.password(passwordEncoder.encode(request.getPassword()))
										.address(request.getAddress())
										.contact(request.getContact())
										.image(DEFAULT_FILE_URL)
										.introduction(request.getIntroduction())
										.userRole(UserType.of(request.getRole()))
										.centerSideInfo(centerSideInfo)
										.build())
							 .getId();

	}

	@Transactional
	public Long modifyProfile(MultipartFile file, Request.Profile request) throws IOException {
		Users user = authService.getCurUser();

		String newImage = !file.isEmpty() ? registerImage(file) : DEFAULT_FILE_URL;

		user.changeUserInfo(
			request.getNickname(),
			passwordEncoder.encode(request.getPassword()),
			request.getAddress(),
			request.getContact(),
			newImage,
			request.getIntroduction()
		);

		if (request.getRole()
				   .equals(UserType.CENTER.getType())) {
			user.getCenterMoreInfo()
				.changeCenterSideInfo(
					request.getOwner(), request.getRegistrationCode()
				);
		}

		return user.getId();
	}

	@Transactional
	public void removeUser() {
		Users user = authService.getCurUser();
		user.deleteEntity();
	}

	public Response.IsUnique isEmailUnique(Request.IsUniqueEmail isUniqueEmail) {
		return new Response.IsUnique(
			!userRepository.existsByEmail(isUniqueEmail.getEmail())
		);
	}

	public Response.IsUnique isNicknameUnique(Request.IsUniqueNickname isUniqueNickname) {
		return new Response.IsUnique(
			!userRepository.existsByNickname(isUniqueNickname.getNickname())
		);
	}

	private String registerImage(MultipartFile newImage) throws IOException {
		return uploadService.upload(newImage, DIRNAME);
	}

	// TODO : 다른 도메인과의 연관 관계도 도메인 Users로 교체되면 제거할 예정
	public CurUser getCurUser() {
		Optional<Member> member = getCurMember();
		Optional<Center> center = getCurCenter();

		if (member.isPresent()) {
			return CurUser.toResponse(member.get());
		}

		if (center.isPresent()) {
			return CurUser.toResponse(center.get());
		}

		throw new NotFoundResourceException(ErrorCode.NOT_FOUND_USER);
	}

	public Optional<Center> getCurCenter() {
		final Authentication authentication = getAuthentication();
		String userRole = getUserRole(authentication);

		if (userRole.equals(UserType.CENTER.getKey())) {
			return centerRepository.findByEmailAndIsDeletedFalse(
				authentication.getName());
		}

		return Optional.empty();
	}

	public Optional<Member> getCurMember() {
		final Authentication authentication = getAuthentication();
		String userRole = getUserRole(authentication);

		if (userRole.equals(UserType.MEMBER.getKey())) {
			return memberRepository.findByEmailAndIsDeletedFalse(
				authentication.getName());
		}

		return Optional.empty();
	}

	private Authentication getAuthentication() {
		final Authentication authentication = SecurityContextHolder.getContext()
																   .getAuthentication();

		if (authentication == null || authentication.getName() == null) {
			throw new RuntimeException("No authentication information.");
		}

		return authentication;
	}

	private String getUserRole(Authentication authentication) {
		List<String> authorities = new ArrayList<>();
		for (GrantedAuthority auth : authentication.getAuthorities()) {
			authorities.add(auth.getAuthority());
		}

		return authorities.get(0);
	}

}
