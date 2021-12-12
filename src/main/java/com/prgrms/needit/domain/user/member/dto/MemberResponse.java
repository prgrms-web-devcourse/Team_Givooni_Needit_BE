package com.prgrms.needit.domain.user.member.dto;

import com.prgrms.needit.domain.user.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
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
