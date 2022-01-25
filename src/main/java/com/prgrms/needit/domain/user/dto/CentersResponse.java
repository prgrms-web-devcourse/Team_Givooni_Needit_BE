package com.prgrms.needit.domain.user.dto;

import com.prgrms.needit.domain.center.entity.Center;
import lombok.Getter;

@Getter
public class CentersResponse {

	private Long centerId;
	private String name;
	private String profileImageUrl;

	public CentersResponse(Center center) {
		this.centerId = center.getId();
		this.name = center.getName();
		this.profileImageUrl = center.getProfileImageUrl();
	}
}