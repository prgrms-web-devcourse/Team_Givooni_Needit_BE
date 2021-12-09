package com.prgrms.needit.domain.member.dto;

import javax.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class MemberUpdateRequest {

	@NotBlank
	private String email;

	@NotBlank
	private String nickname;

	@NotBlank
	private String password;

	@NotBlank
	private String address;

	@NotBlank
	private String contact;

	@NotBlank
	private String profileImageUrl;

}
