package com.prgrms.needit.common.domain.dto;

import javax.validation.constraints.NotBlank;
import lombok.Getter;

public class IsUniqueRequest {

	@Getter
	public static class Email {

		@NotBlank
		private String email;

		public Email(String email) {
			this.email = email;
		}
	}

	@Getter
	public static class Nickname {

		@NotBlank
		private String nickname;

		public Nickname(String nickname) {
			this.nickname = nickname;
		}
	}

}
