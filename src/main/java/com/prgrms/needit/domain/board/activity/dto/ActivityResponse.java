package com.prgrms.needit.domain.board.activity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.prgrms.needit.domain.board.activity.entity.Activity;
import com.prgrms.needit.domain.board.activity.entity.enums.ActivityType;
import com.prgrms.needit.domain.user.center.dto.CenterResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class ActivityResponse {

	@JsonProperty("id")
	private final Long id;

	@JsonProperty("writer")
	private final ActivityWriterInfo writer;

	@JsonProperty("title")
	private final String title;

	@JsonProperty("content")
	private final String content;

	@JsonProperty("postType")
	private final ActivityType activityType;

	@JsonProperty("center")
	private final CenterResponse centerResponse;

	@JsonProperty("createdAt")
	private final LocalDateTime createdAt;

	@JsonProperty("updatedAt")
	private final LocalDateTime updatedAt;

	@JsonProperty("comments")
	private final List<ActivityCommentResponse> comments = new ArrayList<>();

	@JsonProperty("images")
	private final List<ActivityImageResponse> images = new ArrayList<>();

	public ActivityResponse(Activity activity) {
		this.id = activity.getId();
		this.writer = ActivityWriterInfo.ofCenter(activity.getCenter());
		this.title = activity.getTitle();
		this.content = activity.getContent();
		this.activityType = activity.getActivityType();
		this.centerResponse = new CenterResponse(activity.getCenter());
		this.createdAt = activity.getCreatedAt();
		this.updatedAt = activity.getUpdatedAt();
		activity.getComments()
				.stream()
				.filter(comment -> !comment.isDeleted())
				.map(ActivityCommentResponse::new)
				.forEach(comments::add);
		activity.getImages()
				.stream()
				.filter(image -> !image.isDeleted())
				.map(ActivityImageResponse::new)
				.forEach(images::add);
	}

}
