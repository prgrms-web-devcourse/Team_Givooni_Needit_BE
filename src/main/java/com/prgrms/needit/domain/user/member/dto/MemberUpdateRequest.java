package com.prgrms.needit.domain.user.member.dto;

import com.prgrms.needit.domain.user.member.entity.Member;
import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
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

	private String introduction;

	@Builder
	public MemberUpdateRequest(
		String email,
		String nickname,
		String password,
		String address,
		String contact,
		String introduction
	) {
		this.email = email;
		this.nickname = nickname;
		this.password = password;
		this.address = address;
		this.contact = contact;
		this.introduction = introduction;
	}

	public Member toEntity(String password) {
		return Member.builder()
					 .email(this.email)
					 .nickname(this.nickname)
					 .password(password)
					 .address(this.address)
					 .contact(this.contact)
					 .build();
	}

}
