package com.prgrms.needit.domain.member.dto;

import com.prgrms.needit.domain.member.entity.Member;
import lombok.Getter;

@Getter
public class MemberDetailResponse {

	private Long memberId;

	private String email;

	private String nickname;

	private String password;

	private String address;

	private String contact;

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
