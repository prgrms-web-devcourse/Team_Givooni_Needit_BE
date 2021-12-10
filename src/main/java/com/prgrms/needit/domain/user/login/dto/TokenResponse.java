package com.prgrms.needit.domain.user.login.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TokenResponse {

	private String grantType;
	private String accessToken;

	@Builder
	public TokenResponse(String grantType, String accessToken) {
		this.grantType = grantType;
		this.accessToken = accessToken;
	}
}
