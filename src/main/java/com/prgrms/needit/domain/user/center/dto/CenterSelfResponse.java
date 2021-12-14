package com.prgrms.needit.domain.user.center.dto;

import com.prgrms.needit.domain.user.center.entity.Center;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CenterSelfResponse {

	private Long centerId;

	private String email;

	private String name;

	private String contact;

	private String address;

	private String profileImageUrl;

	private String owner;

	private String registrationCode;

	private String introduction;

	public CenterSelfResponse(Center center) {
		this.centerId = center.getId();
		this.email = center.getEmail();
		this.name = center.getName();
		this.contact = center.getContact();
		this.address = center.getAddress();
		this.profileImageUrl = center.getProfileImageUrl();
		this.owner = center.getOwner();
		this.registrationCode = center.getRegistrationCode();
		this.introduction = center.getIntroduction();
	}
}
