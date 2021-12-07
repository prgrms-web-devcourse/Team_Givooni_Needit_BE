package com.prgrms.needit.domain.notification.entity.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.prgrms.needit.domain.notification.entity.Notification;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class NotificationResponse {

	@JsonProperty("id")
	private final long id;

	@JsonProperty("createdAt")
	private final LocalDateTime createdAt;

	@JsonProperty("notifiedUsername")
	private final String username;

	@JsonProperty("content")
	private final String content;

	@JsonProperty("accessLink")
	private final String accessLink;

	@JsonProperty("isChecked")
	private final boolean isChecked;

	public NotificationResponse(Notification notification) {
		this.id = notification.getId();
		this.createdAt = notification.getCreatedAt();
		this.username = notification.getUsername();
		this.content = notification.getNotificationContent();
		this.accessLink = notification.getAccessLink();
		this.isChecked = notification.isChecked();
	}

}
