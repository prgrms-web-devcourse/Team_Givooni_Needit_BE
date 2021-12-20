package com.prgrms.needit.domain.user.user.dto;

import com.prgrms.needit.common.domain.dto.DonationsResponse;
import com.prgrms.needit.domain.user.center.dto.CentersResponse;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserResponse {

	private CurUser myProfile;
	private List<DonationsResponse> myPost = new ArrayList<>();
	private List<CentersResponse> myFavorite = new ArrayList<>();

	public UserResponse(
		CurUser myProfile,
		List<DonationsResponse> myPost,
		List<CentersResponse> myFavorite
	) {
		this.myProfile = myProfile;
		this.myPost = myPost;
		this.myFavorite = myFavorite;
	}
}
