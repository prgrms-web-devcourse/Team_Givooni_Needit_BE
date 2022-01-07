package com.prgrms.needit.domain.user.user.dto;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LogoutRequest {
	@NotBlank
	private String accessToken;

	@NotBlank
	private String refreshToken;
}
