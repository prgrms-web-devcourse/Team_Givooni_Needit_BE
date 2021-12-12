package com.prgrms.needit.domain.notification.entity.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum NotificationContentType {
	// TODO: inject notification service for every features.
	DONATION,
	WISH,
	DONATION_COMMENT,
	WISH_COMMENT,
	UNREAD_CHATS
}
