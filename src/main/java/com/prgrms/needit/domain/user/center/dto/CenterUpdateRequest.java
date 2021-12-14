package com.prgrms.needit.domain.user.center.dto;

import com.prgrms.needit.domain.user.center.entity.Center;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CenterUpdateRequest {

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

	private String introduction;

	public CenterUpdateRequest(
		String email,
		String password,
		String name,
		String contact,
		String address,
		String profileImageUrl,
		String owner,
		String registrationCode,
		String introduction
	) {
		this.email = email;
		this.password = password;
		this.name = name;
		this.contact = contact;
		this.address = address;
		this.profileImageUrl = profileImageUrl;
		this.owner = owner;
		this.registrationCode = registrationCode;
		this.introduction = introduction;
	}

}
