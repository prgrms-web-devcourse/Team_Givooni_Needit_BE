package com.prgrms.needit.domain.user.center.dto;

import com.prgrms.needit.domain.user.center.entity.Center;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
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
	private String owner;

	@NotBlank
	private String registrationCode;

	public CenterCreateRequest(
		String email,
		String password,
		String name,
		String contact,
		String address,
		String owner,
		String registrationCode
	) {
		this.email = email;
		this.password = password;
		this.name = name;
		this.contact = contact;
		this.address = address;
		this.owner = owner;
		this.registrationCode = registrationCode;
	}

	public Center toEntity(String password) {
		return Center.builder()
					 .email(this.email)
					 .password(password)
					 .name(this.name)
					 .contact(this.contact)
					 .address(this.address)
					 .owner(this.owner)
					 .registrationCode(this.registrationCode)
					 .build();
	}

}
