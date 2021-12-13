package com.prgrms.needit.domain.user.email.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EmailRequest {

	private String email;

	public EmailRequest(String email) {
		this.email = email;
	}
}
