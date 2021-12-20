package com.prgrms.needit.domain.user.center.dto;

import com.prgrms.needit.domain.user.center.entity.Center;
import lombok.Getter;

@Getter
public class CenterResponse {

	private Long centerId;
	private String name;
	private String contact;
	private String address;
	private String profileImageUrl;
	private String owner;
	private String introduction;

	public CenterResponse(Center center) {
		this.centerId = center.getId();
		this.name = center.getName();
		this.contact = center.getContact();
		this.address = center.getAddress();
		this.profileImageUrl = center.getProfileImageUrl();
		this.owner = center.getOwner();
		this.introduction = center.getIntroduction();
	}
}
