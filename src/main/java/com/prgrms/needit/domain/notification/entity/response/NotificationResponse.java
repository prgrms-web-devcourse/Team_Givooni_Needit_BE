package com.prgrms.needit.domain.notification.entity.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.prgrms.needit.domain.notification.entity.Notification;
import com.prgrms.needit.domain.notification.entity.enums.NotificationContentType;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class NotificationResponse {

	@JsonProperty("id")
	private final Long id;

	@JsonProperty("createdAt")
	private final LocalDateTime createdAt;

	@JsonProperty("resourceType")
	private final NotificationContentType contentType;

	@JsonProperty("resourceId")
	private final Long resourceId;

	@JsonProperty("previewMessage")
	private final String previewMessage;

	@JsonProperty("isChecked")
	private final boolean isChecked;

	public NotificationResponse(Notification notification) {
		this.id = notification.getId();
		this.createdAt = notification.getCreatedAt();
		this.contentType = notification.getNotifiedContentType();
		this.resourceId = notification.getNotifiedContentValue();
		this.previewMessage = notification.getPreviewMessage();
		this.isChecked = notification.isChecked();
	}

}
