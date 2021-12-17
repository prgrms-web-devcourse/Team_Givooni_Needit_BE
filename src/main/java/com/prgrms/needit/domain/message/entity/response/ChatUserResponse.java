package com.prgrms.needit.domain.message.entity.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.prgrms.needit.domain.user.center.entity.Center;
import com.prgrms.needit.domain.user.member.entity.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatUserResponse {

	@JsonProperty("id")
	private final Long id;

	@JsonProperty("name")
	private final String name;

	@JsonProperty("profileImageUrl")
	private final String profileImageUrl;

	public static ChatUserResponse ofMember(Member member) {
		return new ChatUserResponse(
			member.getId(),
			member.getNickname(),
			member.getProfileImageUrl());
	}

	public static ChatUserResponse ofCenter(Center center) {
		return new ChatUserResponse(
			center.getId(),
			center.getName(),
			center.getProfileImageUrl());
	}

}
