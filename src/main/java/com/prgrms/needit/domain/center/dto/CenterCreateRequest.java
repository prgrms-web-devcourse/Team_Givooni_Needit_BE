package com.prgrms.needit.domain.center.dto;

import com.prgrms.needit.domain.center.entity.Center;
import javax.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CenterCreateRequest {

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

	public Center toEntity(String password) {
		return Center.builder()
					 .email(this.email)
					 .password(password)
					 .name(this.name)
					 .contact(this.contact)
					 .address(this.address)
					 .profileImageUrl(this.profileImageUrl)
					 .owner(this.owner)
					 .registrationCode(this.registrationCode)
					 .build();
	}

}
