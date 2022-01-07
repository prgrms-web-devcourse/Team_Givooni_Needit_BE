package com.prgrms.needit.domain.user.user.service;

import com.prgrms.needit.common.config.jwt.JwtTokenProvider;
import com.prgrms.needit.common.domain.dto.DonationsResponse;
import com.prgrms.needit.common.enums.UserType;
import com.prgrms.needit.common.error.ErrorCode;
import com.prgrms.needit.common.error.exception.NotFoundResourceException;
import com.prgrms.needit.common.error.exception.RefreshTokenException;
import com.prgrms.needit.domain.board.donation.repository.DonationRepository;
import com.prgrms.needit.domain.board.wish.repository.DonationWishRepository;
import com.prgrms.needit.domain.user.center.dto.CentersResponse;
import com.prgrms.needit.domain.user.center.entity.Center;
import com.prgrms.needit.domain.user.center.repository.CenterRepository;
import com.prgrms.needit.domain.user.favorite.entity.FavoriteCenter;
import com.prgrms.needit.domain.user.favorite.repository.FavoriteCenterRepository;
import com.prgrms.needit.domain.user.member.entity.Member;
import com.prgrms.needit.domain.user.member.repository.MemberRepository;
import com.prgrms.needit.domain.user.user.dto.CurUser;
import com.prgrms.needit.domain.user.user.dto.Request;
import com.prgrms.needit.domain.user.user.dto.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
public class UserService {

	private final MemberRepository memberRepository;
	private final CenterRepository centerRepository;
	private final DonationRepository donationRepository;
	private final DonationWishRepository donationWishRepository;
	private final FavoriteCenterRepository favoriteCenterRepository;
	private final JwtTokenProvider jwtTokenProvider;
	private final AuthenticationManagerBuilder authManagerBuilder;
	private final RedisTemplate<String, String> redisTemplate;

	public Response.TokenInfo login(Request.Login login) {
		findActiveMemberAndCenter(login.getEmail());

		UsernamePasswordAuthenticationToken authenticationToken =
			new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword());

		Authentication authentication = authManagerBuilder.getObject()
														  .authenticate(authenticationToken);

		return getTokenResponse(authentication);
	}

	public Response.TokenInfo reissue(Request.Reissue reissue) {

		Authentication authentication = getAuthentication(
			reissue.getAccessToken(), reissue.getRefreshToken()
		);

		String refreshToken = redisTemplate.opsForValue()
										   .get("RT:" + authentication.getName());

		if (ObjectUtils.isEmpty(refreshToken)) {
			throw new RefreshTokenException(ErrorCode.NOT_FOUND_REFRESH_TOKEN);
		}

		if (!refreshToken.equals(reissue.getRefreshToken())) {
			throw new RefreshTokenException(ErrorCode.NOT_MATCH_REFRESH_TOKEN);
		}

		return getTokenResponse(authentication);
	}

	public void logout(Request.Logout logout) {
		Authentication authentication = getAuthentication(
			logout.getAccessToken(), logout.getRefreshToken()
		);

		if (redisTemplate.opsForValue()
						 .get("RT:" + authentication.getName()) != null) {
			redisTemplate.delete("RT:" + authentication.getName());
		}

		Long expiration = jwtTokenProvider.getExpiration(logout.getAccessToken());
		redisTemplate.opsForValue()
					 .set(
						 logout.getAccessToken(), "logout",
						 expiration, TimeUnit.MILLISECONDS
					 );

	}


	public Response.UserInfo getUserInfo() {
		Optional<Member> member = getCurMember();
		Optional<Center> center = getCurCenter();

		CurUser curUser = null;
		List<DonationsResponse> myPost = new ArrayList<>();
		List<CentersResponse> myFavorite = new ArrayList<>();

		if (member.isPresent()) {
			curUser = CurUser.toResponse(member.get());
			myPost = donationRepository.findAllByMemberAndIsDeletedFalse(member.get())
									   .stream()
									   .map(DonationsResponse::toResponse)
									   .collect(Collectors.toList());
			myFavorite = favoriteCenterRepository.findAllByMemberOrderByCreatedAt(member.get())
												 .stream()
												 .map(FavoriteCenter::getCenter)
												 .map(CentersResponse::new)
												 .collect(Collectors.toList());
		} else if (center.isPresent()) {
			curUser = CurUser.toResponse(center.get());
			myPost = donationWishRepository.findAllByCenterAndIsDeletedFalse(center.get())
										   .stream()
										   .map(DonationsResponse::toResponse)
										   .collect(Collectors.toList());
			myFavorite = null;
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
		String inputEmail = isUniqueEmail.getEmail();
		boolean isUniqueMember = !memberRepository.existsByEmail(inputEmail);
		boolean isUniqueCenter = !centerRepository.existsByEmail(inputEmail);

		return new Response.IsUnique(isUniqueMember && isUniqueCenter);
	}

	public Response.IsUnique isNicknameUnique(Request.IsUniqueNickname isUniqueNickname) {
		return new Response.IsUnique(
			!memberRepository.existsByNickname(isUniqueNickname.getNickname())
		);
	}

	private Response.TokenInfo getTokenResponse(Authentication authentication) {
		Response.TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);

		redisTemplate.opsForValue()
					 .set(
						 "RT:" + authentication.getName(), tokenInfo.getRefreshToken(),
						 tokenInfo.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS
					 );

		return tokenInfo;
	}

	private Authentication getAuthentication(String accessToken, String refreshToken) {
		if (!jwtTokenProvider.validateToken(refreshToken)) {
			throw new RefreshTokenException(ErrorCode.INVALID_REFRESH_TOKEN);
		}

		return jwtTokenProvider.getAuthentication(accessToken);
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

	private void findActiveMemberAndCenter(String email) {
		Optional<Member> member = memberRepository
			.findByEmailAndIsDeletedFalse(email);

		Optional<Center> center = centerRepository
			.findByEmailAndIsDeletedFalse(email);

		if (member.isEmpty() && center.isEmpty()) {
			throw new NotFoundResourceException(ErrorCode.NOT_FOUND_USER);
		}
	}

}
