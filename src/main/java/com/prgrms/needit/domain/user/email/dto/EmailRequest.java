package com.prgrms.needit.domain.user.email.dto;

import lombok.Getter;

@Getter
public class EmailRequest {

	private final String email;

	public EmailRequest(String email) {
		this.email = email;
	}
}
