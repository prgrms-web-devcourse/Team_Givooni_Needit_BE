package com.prgrms.needit.domain.board.activity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.prgrms.needit.domain.board.activity.entity.ActivityComment;
import io.jsonwebtoken.lang.Assert;
import lombok.Getter;

@Getter
public class ActivityCommentResponse {

	@JsonProperty("id")
	private final Long id;

	@JsonProperty("writer")
	private final ActivityWriterInfo writer;

	@JsonProperty("comment")
	private final String comment;

	public ActivityCommentResponse(ActivityComment comment) {
		validateInfo(comment);
		this.id = comment.getId();
		this.comment = comment.getComment();
		this.writer = comment.getWriterInfo();
	}

	private void validateInfo(
		ActivityComment comment) {
		Assert.notNull(comment, "Activity comment cannot be null.");
	}
}
