package com.prgrms.needit.domain.board.activity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.prgrms.needit.common.domain.dto.CommentResponse;
import com.prgrms.needit.domain.board.activity.entity.Activity;
import com.prgrms.needit.domain.board.activity.entity.enums.ActivityType;
import com.prgrms.needit.domain.center.entity.Center;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class ActivityResponse {

	@JsonProperty("id")
	private final Long id;

	@JsonProperty("title")
	private final String title;

	@JsonProperty("content")
	private final String content;

	@JsonProperty("boardType")
	private final ActivityType activityType;

	@JsonProperty("userId")
	private final Long userId;

	@JsonProperty("userName")
	private final String username;

	@JsonProperty("userImage")
	private final String profileImageUrl;

	@JsonProperty("createdDate")
	private final LocalDateTime createdAt;

	@JsonProperty("updatedDate")
	private final LocalDateTime updatedAt;

	@JsonProperty("comments")
	private final List<CommentResponse> comments = new ArrayList<>();

	@JsonProperty("images")
	private final List<ActivityImageResponse> images = new ArrayList<>();

	public ActivityResponse(Activity activity) {
		this.id = activity.getId();
		Center center = activity.getCenter();
		this.userId = center.getId();
		this.username = center.getName();
		this.profileImageUrl = center.getProfileImageUrl();
		this.title = activity.getTitle();
		this.content = activity.getContent();
		this.activityType = activity.getActivityType();
		this.createdAt = activity.getCreatedAt();
		this.updatedAt = activity.getUpdatedAt();
		activity.getComments()
				.stream()
				.filter(comment -> !comment.isDeleted())
				.map(CommentResponse::toResponse)
				.forEach(comments::add);
		activity.getImages()
				.stream()
				.filter(image -> !image.isDeleted())
				.map(ActivityImageResponse::new)
				.forEach(images::add);
	}

}
