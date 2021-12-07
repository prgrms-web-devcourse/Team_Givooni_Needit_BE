package com.prgrms.needit.domain.notification.entity;

import com.prgrms.needit.common.domain.entity.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table(name = "notification")
@Entity
@NoArgsConstructor
public class Notification extends BaseEntity {

	@Column(name = "username", nullable = false)
	private String username;

	@Column(name = "content", nullable = false)
	private String notificationContent;

	@Column(name = "link", nullable = false)
	private String accessLink;

	@Column(name = "is_checked", nullable = false)
	private boolean isChecked;

	@Builder
	public Notification(String username, String notificationContent, String accessLink) {
		this.username = username;
		this.notificationContent = notificationContent;
		this.accessLink = accessLink;
		isChecked = false;
	}
}
