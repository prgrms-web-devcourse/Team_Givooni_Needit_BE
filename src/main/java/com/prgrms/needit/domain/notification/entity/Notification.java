package com.prgrms.needit.domain.notification.entity;

import com.prgrms.needit.common.domain.entity.BaseEntity;
import com.prgrms.needit.common.enums.UserType;
import com.prgrms.needit.domain.notification.entity.enums.NotificationContentType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

@Getter
@Table(name = "notification")
@Entity
@NoArgsConstructor
public class Notification extends BaseEntity {

	@Column(name = "notified_user_id", nullable = false)
	private Long userId;

	@Enumerated(EnumType.STRING)
	@Column(name = "notified_user_type", nullable = false)
	private UserType userType;

	@Enumerated(EnumType.STRING)
	@Column(name = "notified_content_type", nullable = false)
	private NotificationContentType notifiedContentType;

	@Column(name = "notified_content_value", nullable = false)
	private Long notifiedContentId;

	@Column(name = "preview_message", nullable = false)
	private String previewMessage;

	@Column(name = "checked", nullable = false)
	private boolean checked;

	private void validateInfo(
		Long notifiedUserId,
		UserType userType,
		NotificationContentType notificationContentType,
		Long notifiedContentId,
		String previewMessage
	) {
		Assert.isTrue(notifiedUserId > 0, "Notified user id cannot be 0 or negative.");
		Assert.notNull(userType, "Notified user type cannot be null.");
		Assert.notNull(notificationContentType, "Notification content type cannot be null.");
		Assert.isTrue(notifiedContentId > 0, "Notified content id cannot be 0 or negative.");
		Assert.hasText(previewMessage, "Preview message cannot be null or blank.");
	}

	@Builder
	public Notification(
		Long userId,
		UserType userType,
		NotificationContentType notifiedContentType,
		Long notifiedContentId,
		String previewMessage
	) {
		validateInfo(userId, userType, notifiedContentType, notifiedContentId, previewMessage);
		this.userId = userId;
		this.userType = userType;
		this.notifiedContentType = notifiedContentType;
		this.notifiedContentId = notifiedContentId;
		this.previewMessage = previewMessage;
		this.checked = false;
	}

	public void check() {
		this.checked = true;
	}

}
