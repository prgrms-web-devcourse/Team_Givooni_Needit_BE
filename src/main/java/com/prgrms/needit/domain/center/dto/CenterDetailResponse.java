package com.prgrms.needit.domain.center.dto;

import com.prgrms.needit.domain.center.entity.Center;
import lombok.Getter;

@Getter
public class CenterDetailResponse {

	private Long centerId;

	private String email;

	private String password;

	private String name;

	private String contact;

	private String address;

	private String profileImageUrl;

	private String owner;

	private String registrationCode;

	public CenterDetailResponse() {
	}

	public CenterDetailResponse(Center center) {
		this.centerId = center.getId();
		this.email = center.getEmail();
		this.password = center.getPassword();
		this.name = center.getName();
		this.contact = center.getContact();
		this.address = center.getAddress();
		this.profileImageUrl = center.getProfileImageUrl();
		this.owner = center.getOwner();
		this.registrationCode = center.getRegistrationCode();
	}
}
