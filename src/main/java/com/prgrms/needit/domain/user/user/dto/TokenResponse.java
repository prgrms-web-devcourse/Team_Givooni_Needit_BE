package com.prgrms.needit.domain.user.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TokenResponse {

	private String grantType;
	private String accessToken;
	private String refreshToken;
	private Long refreshTokenExpirationTime;

	@Builder
	public TokenResponse(
		String grantType,
		String accessToken,
		String refreshToken,
		Long refreshTokenExpirationTime
	) {
		this.grantType = grantType;
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.refreshTokenExpirationTime = refreshTokenExpirationTime;
	}
}
