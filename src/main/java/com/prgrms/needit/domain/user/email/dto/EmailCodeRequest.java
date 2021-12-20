package com.prgrms.needit.domain.user.email.dto;

import javax.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class EmailCodeRequest {

	@NotBlank
	private final String email;

	@NotBlank
	private final String code;

	public EmailCodeRequest(String email, String code) {
		this.email = email;
		this.code = code;
	}
}
