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
	private Long notifiedUserId;

	@Enumerated(EnumType.STRING)
	@Column(name = "notified_user_type", nullable = false)
	private UserType notifiedUserType;

	@Enumerated(EnumType.STRING)
	@Column(name = "notified_content_type", nullable = false)
	private NotificationContentType notifiedContentType;

	@Column(name = "notified_content_value", nullable = false)
	private Long notifiedContentValue;

	@Column(name = "preview_message", nullable = false)
	private String previewMessage;

	@Column(name = "is_checked", nullable = false)
	private boolean isChecked;

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
		Long notifiedUserId,
		UserType notifiedUserType,
		NotificationContentType notifiedContentType,
		Long notifiedContentValue,
		String previewMessage
	) {
		validateInfo(notifiedUserId, notifiedUserType, notifiedContentType, notifiedContentValue, previewMessage);
		this.notifiedUserId = notifiedUserId;
		this.notifiedUserType = notifiedUserType;
		this.notifiedContentType = notifiedContentType;
		this.notifiedContentValue = notifiedContentValue;
		this.previewMessage = previewMessage;
		this.isChecked = false;
	}

	public void check() {
		this.isChecked = true;
	}

}
