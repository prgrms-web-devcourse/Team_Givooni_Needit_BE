package com.prgrms.needit.domain.member.dto;

import com.prgrms.needit.domain.member.entity.Member;
import javax.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class MemberCreateRequest {

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

	public Member toEntity(String password) {
		return Member.builder()
					 .email(this.email)
					 .nickname(this.nickname)
					 .password(password)
					 .address(this.address)
					 .contact(this.contact)
					 .profileImageUrl(this.profileImageUrl)
					 .build();
	}

}
