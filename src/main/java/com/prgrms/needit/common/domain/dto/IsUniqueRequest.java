package com.prgrms.needit.common.domain.dto;

import lombok.Getter;

public class IsUniqueRequest {

	@Getter
	public static class Email {

		private String email;

		public Email(String email) {
			this.email = email;
		}
	}

	@Getter
	public static class Nickname {

		private String nickname;

		public Nickname(String nickname) {
			this.nickname = nickname;
		}
	}

}
