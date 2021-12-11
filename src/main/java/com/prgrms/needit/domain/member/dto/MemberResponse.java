package com.prgrms.needit.domain.member.dto;

import com.prgrms.needit.domain.member.entity.Member;
import lombok.Getter;

@Getter
public class MemberResponse {

	private Long memberId;

	private String nickname;

	private String profileImageUrl;

	public MemberResponse(Member member) {
		this.memberId = member.getId();
		this.nickname = member.getNickname();
		this.profileImageUrl = member.getProfileImageUrl();
	}
}