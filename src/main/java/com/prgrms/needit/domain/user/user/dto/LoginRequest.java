package com.prgrms.needit.domain.user.user.dto;

import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginRequest {

	@NotBlank
	private String email;

	@NotBlank
	private String password;

	@Builder
	private LoginRequest(String email, String password) {
		this.email = email;
		this.password = password;
	}

}
