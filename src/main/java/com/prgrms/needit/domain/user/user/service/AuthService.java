package com.prgrms.needit.domain.user.user.service;

import com.prgrms.needit.common.config.jwt.JwtTokenProvider;
import com.prgrms.needit.common.error.ErrorCode;
import com.prgrms.needit.common.error.exception.NotFoundResourceException;
import com.prgrms.needit.common.error.exception.RefreshTokenException;
import com.prgrms.needit.domain.user.user.dto.Request;
import com.prgrms.needit.domain.user.user.dto.Response;
import com.prgrms.needit.domain.user.user.entity.Users;
import com.prgrms.needit.domain.user.user.repository.UserRepository;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;
	private final JwtTokenProvider jwtTokenProvider;
	private final AuthenticationManagerBuilder authManagerBuilder;
	private final RedisTemplate<String, String> redisTemplate;

	@Transactional(readOnly = true)
	public Users getCurUser() {
		return userRepository.findByEmailAndIsDeletedFalse(getCurUserEmail())
							 .orElseThrow(
								 () -> new NotFoundResourceException(ErrorCode.NOT_FOUND_USER)
							 );
	}

	public Response.TokenInfo login(Request.Login login) {
		findActiveUser(login.getEmail());

		UsernamePasswordAuthenticationToken authenticationToken =
			new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword());

		Authentication authentication = authManagerBuilder.getObject()
														  .authenticate(authenticationToken);

		return getTokenResponse(authentication);
	}

	public Response.TokenInfo reissue(Request.Reissue reissue) {
		Authentication authentication = getAuthenticationByToken(
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
		Authentication authentication = getAuthenticationByToken(
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

	private String getCurUserEmail() {
		final Authentication authentication = SecurityContextHolder.getContext()
																   .getAuthentication();

		if (authentication == null || authentication.getName() == null) {
			throw new RuntimeException("No authentication information.");
		}

		return authentication.getName();
	}


	private Authentication getAuthenticationByToken(String accessToken, String refreshToken) {
		if (!jwtTokenProvider.validateToken(refreshToken)) {
			throw new RefreshTokenException(ErrorCode.INVALID_REFRESH_TOKEN);
		}

		return jwtTokenProvider.getAuthentication(accessToken);
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

	private void findActiveUser(String email) {
		userRepository.findByEmailAndIsDeletedFalse(email)
					  .orElseThrow(
						  () -> new NotFoundResourceException(ErrorCode.NOT_FOUND_USER));
	}
}
