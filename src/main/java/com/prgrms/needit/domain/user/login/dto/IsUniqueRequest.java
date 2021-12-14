package com.prgrms.needit.domain.user.login.dto;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class IsUniqueRequest {

	@Getter
	@NoArgsConstructor
	public static class Email {

		@NotBlank
		private String email;

		public Email(String email) {
			this.email = email;
		}
	}

	@Getter
	@NoArgsConstructor
	public static class Nickname {

		@NotBlank
		private String nickname;

		public Nickname(String nickname) {
			this.nickname = nickname;
		}
	}

}
