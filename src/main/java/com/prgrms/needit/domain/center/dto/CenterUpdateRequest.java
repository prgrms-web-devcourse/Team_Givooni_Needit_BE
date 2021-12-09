package com.prgrms.needit.domain.center.dto;

import javax.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
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

}
