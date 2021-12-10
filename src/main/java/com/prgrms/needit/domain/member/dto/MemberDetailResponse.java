package com.prgrms.needit.domain.member.dto;

import com.prgrms.needit.domain.member.entity.Member;
import javax.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class MemberDetailResponse {

	@NotBlank
	private Long memberId;

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

	public MemberDetailResponse() {
	}

	public MemberDetailResponse(Member member) {
		this.memberId = member.getId();
		this.email = member.getEmail();
		this.nickname = member.getNickname();
		this.password = member.getPassword();
		this.address = member.getAddress();
		this.contact = member.getContact();
		this.profileImageUrl = member.getProfileImageUrl();
	}
}
