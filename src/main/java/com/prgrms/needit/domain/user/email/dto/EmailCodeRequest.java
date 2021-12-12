package com.prgrms.needit.domain.user.email.dto;

import lombok.Getter;

@Getter
public class EmailCodeRequest {

	private final String email;
	private final String code;

	public EmailCodeRequest(String email, String code) {
		this.email = email;
		this.code = code;
	}
}
