package com.prgrms.needit.domain.board.activity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.prgrms.needit.common.enums.UserType;
import com.prgrms.needit.domain.user.center.entity.Center;
import com.prgrms.needit.domain.user.member.entity.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ActivityWriterInfo {

	@JsonProperty("id")
	private final Long writerId;

	@JsonProperty("type")
	private final UserType writerType;

	@JsonProperty("name")
	private final String writerName;

	public static ActivityWriterInfo ofMember(Member member) {
		return new ActivityWriterInfo(
			member.getId(),
			UserType.MEMBER,
			member.getNickname());
	}

	public static ActivityWriterInfo ofCenter(Center center) {
		return new ActivityWriterInfo(
			center.getId(),
			UserType.CENTER,
			center.getName());
	}

}
