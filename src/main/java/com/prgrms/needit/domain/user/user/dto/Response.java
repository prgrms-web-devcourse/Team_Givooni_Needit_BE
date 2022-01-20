package com.prgrms.needit.domain.user.user.dto;

import com.prgrms.needit.common.domain.dto.DonationsResponse;
import com.prgrms.needit.domain.user.center.dto.CentersResponse;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class Response {

	@Getter
	@NoArgsConstructor
	public static class UserInfo {

		private CurUser myProfile;
		private List<DonationsResponse> myPost = new ArrayList<>();
		private List<CentersResponse> myFavorite = new ArrayList<>();

		public UserInfo(
			CurUser myProfile,
			List<DonationsResponse> myPost,
			List<CentersResponse> myFavorite
		) {
			this.myProfile = myProfile;
			this.myPost = myPost;
			this.myFavorite = myFavorite;
		}
	}

	@Getter
	@NoArgsConstructor
	public static class TokenInfo {

		private String grantType;
		private String accessToken;
		private String refreshToken;
		private Long refreshTokenExpirationTime;

		@Builder
		public TokenInfo(
			String grantType,
			String accessToken,
			String refreshToken,
			Long refreshTokenExpirationTime
		) {
			this.grantType = grantType;
			this.accessToken = accessToken;
			this.refreshToken = refreshToken;
			this.refreshTokenExpirationTime = refreshTokenExpirationTime;
		}
	}

	@Getter
	@NoArgsConstructor
	public static class IsUnique {

		private boolean unique;

		public IsUnique(boolean unique) {
			this.unique = unique;
		}
	}

}
