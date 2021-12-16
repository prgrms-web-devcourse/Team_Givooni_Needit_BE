package com.prgrms.needit.domain.message.entity.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.prgrms.needit.domain.user.center.entity.Center;
import com.prgrms.needit.domain.user.member.entity.Member;
import lombok.Getter;

@Getter
public class ChatUserResponse {

	@JsonProperty("id")
	private final Long id;

	@JsonProperty("name")
	private final String name;

	@JsonProperty("profileImageUrl")
	private final String profileImageUrl;

	public ChatUserResponse(Member member) {
		this.id = member.getId();
		this.name = member.getNickname();
		this.profileImageUrl = member.getProfileImageUrl();
	}

	public ChatUserResponse(Center center) {
		this.id = center.getId();
		this.name = center.getName();
		this.profileImageUrl = center.getProfileImageUrl();
	}
}
