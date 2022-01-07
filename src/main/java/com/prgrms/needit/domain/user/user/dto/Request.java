package com.prgrms.needit.domain.user.user.dto;

import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class Request {

	@Getter
	@NoArgsConstructor
	public static class Login {

		@NotBlank
		private String email;

		@NotBlank
		private String password;

		public Login(String email, String password) {
			this.email = email;
			this.password = password;
		}
	}

	@Getter
	@NoArgsConstructor
	public static class Reissue {

		@NotBlank
		private String accessToken;

		@NotBlank
		private String refreshToken;

		public Reissue(String accessToken, String refreshToken) {
			this.accessToken = accessToken;
			this.refreshToken = refreshToken;
		}
	}

	@Getter
	@NoArgsConstructor
	public static class Logout {

		@NotBlank
		private String accessToken;

		@NotBlank
		private String refreshToken;

		public Logout(String accessToken, String refreshToken) {
			this.accessToken = accessToken;
			this.refreshToken = refreshToken;
		}
	}

	@Getter
	@NoArgsConstructor
	public static class IsUniqueEmail {

		@NotBlank
		private String email;

		public IsUniqueEmail(String email) {
			this.email = email;
		}
	}

	@Getter
	@NoArgsConstructor
	public static class IsUniqueNickname {

		@NotBlank
		private String nickname;

		public IsUniqueNickname(String nickname) {
			this.nickname = nickname;
		}
	}

}
