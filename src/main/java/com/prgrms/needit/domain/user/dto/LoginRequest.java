package com.prgrms.needit.domain.user.dto;

import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginRequest {

	@NotNull
	private String email;

	@NotNull
	private String password;

	@Builder
	private LoginRequest(String email, String password) {
		this.email = email;
		this.password = password;
	}

}
