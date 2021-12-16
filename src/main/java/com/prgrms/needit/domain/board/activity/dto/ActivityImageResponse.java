package com.prgrms.needit.domain.board.activity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.prgrms.needit.domain.board.activity.entity.ActivityImage;
import lombok.Getter;

@Getter
public class ActivityImageResponse {

	@JsonProperty("id")
	private final Long id;

	@JsonProperty("url")
	private final String url;

	public ActivityImageResponse(ActivityImage image) {
		this.id = image.getId();
		this.url = image.getUrl();
	}
}
