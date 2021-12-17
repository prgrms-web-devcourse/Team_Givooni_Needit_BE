package com.prgrms.needit.domain.board.activity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.prgrms.needit.common.enums.UserType;
import com.prgrms.needit.domain.user.center.entity.Center;
import com.prgrms.needit.domain.user.member.entity.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ActivityCommentWriterInfo {

	@JsonProperty("id")
	private final Long writerId;

	@JsonProperty("type")
	private final UserType writerType;

	@JsonProperty("name")
	private final String writerName;

	@JsonProperty("profileImageUrl")
	private final String profileImageUrl;

	public static ActivityCommentWriterInfo ofMember(Member member) {
		return new ActivityCommentWriterInfo(
			member.getId(),
			UserType.MEMBER,
			member.getNickname(),
			member.getProfileImageUrl());
	}

	public static ActivityCommentWriterInfo ofCenter(Center center) {
		return new ActivityCommentWriterInfo(
			center.getId(),
			UserType.CENTER,
			center.getName(),
			center.getProfileImageUrl());
	}

}
