package com.prgrms.needit.domain.center.dto;

import com.prgrms.needit.domain.center.entity.Center;
import javax.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CenterDetailResponse {

	@NotBlank
	private Long centerId;

	@NotBlank
	private String email;

	@NotBlank
	private String password;

	@NotBlank
	private String name;

	@NotBlank
	private String contact;

	@NotBlank
	private String address;

	@NotBlank
	private String profileImageUrl;

	@NotBlank
	private String owner;

	@NotBlank
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
