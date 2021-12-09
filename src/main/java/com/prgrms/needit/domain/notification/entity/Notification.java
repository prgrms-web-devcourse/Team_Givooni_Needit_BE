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
	private long notifiedUserId;

	@Enumerated(EnumType.STRING)
	@Column(name = "notified_user_type", nullable = false)
	private UserType notifiedUserType;

	@Enumerated(EnumType.STRING)
	@Column(name = "notified_content_type", nullable = false)
	private NotificationContentType notifiedContentType;

	@Column(name = "notified_content_value", nullable = false)
	private long notifiedContentValue;

	@Column(name = "is_checked", nullable = false)
	private boolean isChecked;

	private void validateInfo(
		long notifiedUserId,
		UserType userType,
		NotificationContentType notificationContentType,
		long notifiedContentId
	) {
		Assert.isTrue(notifiedUserId > 0, "Notified user id cannot be 0 or negative.");
		Assert.notNull(userType, "Notified user type cannot be null.");
		Assert.notNull(notificationContentType, "Notification content type cannot be null.");
		Assert.isTrue(notifiedContentId > 0, "Notified content id cannot be 0 or negative.");
	}

	@Builder
	public Notification(
		long notifiedUserId,
		UserType notifiedUserType,
		NotificationContentType notifiedContentType,
		long notifiedContentValue
	) {
		validateInfo(notifiedUserId, notifiedUserType, notifiedContentType, notifiedContentValue);
		this.notifiedUserId = notifiedUserId;
		this.notifiedUserType = notifiedUserType;
		this.notifiedContentType = notifiedContentType;
		this.notifiedContentValue = notifiedContentValue;
		this.isChecked = false;
	}

	public void check() {
		this.isChecked = true;
	}

}
