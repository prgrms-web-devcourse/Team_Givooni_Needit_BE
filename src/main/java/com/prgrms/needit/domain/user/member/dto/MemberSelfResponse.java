package com.prgrms.needit.domain.user.member.dto;

import com.prgrms.needit.domain.user.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberSelfResponse {

	private Long memberId;
	private String email;
	private String nickname;
	private String password;
	private String address;
	private String contact;
	private String profileImageUrl;

	public MemberSelfResponse(Member member) {
		this.memberId = member.getId();
		this.email = member.getEmail();
		this.nickname = member.getNickname();
		this.password = member.getPassword();
		this.address = member.getAddress();
		this.contact = member.getContact();
		this.profileImageUrl = member.getProfileImageUrl();
	}
}
