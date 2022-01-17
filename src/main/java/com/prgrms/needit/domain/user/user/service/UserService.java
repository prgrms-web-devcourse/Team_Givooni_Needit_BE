package com.prgrms.needit.domain.user.user.service;

import com.prgrms.needit.common.domain.dto.DonationsResponse;
import com.prgrms.needit.common.enums.UserType;
import com.prgrms.needit.common.error.ErrorCode;
import com.prgrms.needit.common.error.exception.NotFoundResourceException;
import com.prgrms.needit.domain.user.center.dto.CentersResponse;
import com.prgrms.needit.domain.user.center.entity.Center;
import com.prgrms.needit.domain.user.center.repository.CenterRepository;
import com.prgrms.needit.domain.user.member.entity.Member;
import com.prgrms.needit.domain.user.member.repository.MemberRepository;
import com.prgrms.needit.domain.user.user.dto.CurUser;
import com.prgrms.needit.domain.user.user.dto.Request;
import com.prgrms.needit.domain.user.user.dto.Response;
import com.prgrms.needit.domain.user.user.entity.Users;
import com.prgrms.needit.domain.user.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final MemberRepository memberRepository;
	private final CenterRepository centerRepository;
	private final AuthService authService;

	public Response.UserInfo getUserInfo() {
		Users user = authService.getCurUser();

		CurUser curUser = null;
		List<DonationsResponse> myPost = new ArrayList<>();
		List<CentersResponse> myFavorite = new ArrayList<>();

		if (user.getUserRole()
				.equals(UserType.MEMBER)) {
			// TODO
		}

		return new Response.UserInfo(curUser, myPost, myFavorite);
	}

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
