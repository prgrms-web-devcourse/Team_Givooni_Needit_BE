package com.prgrms.needit.domain.user.member.dto;

import com.prgrms.needit.domain.user.member.entity.Member;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberRequest {

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

	private String introduction;

	public MemberRequest(
		String email,
		String nickname,
		String password,
		String address,
		String contact,
		String profileImageUrl,
		String introduction
	) {
		this.email = email;
		this.nickname = nickname;
		this.password = password;
		this.address = address;
		this.contact = contact;
		this.profileImageUrl = profileImageUrl;
		this.introduction = introduction;
	}

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
