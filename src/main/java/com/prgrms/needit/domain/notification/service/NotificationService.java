package com.prgrms.needit.domain.notification.service;

import com.prgrms.needit.common.enums.UserType;
import com.prgrms.needit.domain.notification.entity.Notification;
import com.prgrms.needit.domain.notification.entity.enums.NotificationContentType;
import com.prgrms.needit.domain.notification.entity.response.NotificationResponse;
import com.prgrms.needit.domain.notification.exception.NotificationNotFoundException;
import com.prgrms.needit.domain.notification.repository.NotificationRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class NotificationService {

	private final NotificationRepository notificationRepository;
	private final SimpMessagingTemplate messagingTemplate;

	public void createAndSendNotification(
		String username,
		Long userId,
		UserType userType,
		NotificationContentType notificationContentType,
		Long notificationContentValue,
		String previewMessage
	) {
		Notification notification = notificationRepository.save(Notification
			.builder()
			.userId(userId)
			.userType(userType)
			.notifiedContentType(notificationContentType)
			.notifiedContentId(notificationContentValue)
			.previewMessage(previewMessage)
			.build());
		messagingTemplate.convertAndSendToUser(
			username,
			"/topic/notifications",
			new NotificationResponse(notification)
		);
	}

	public void checkNotification(
		Long notificationId
	) {
		Notification notification = notificationRepository
			.findById(notificationId)
			.orElseThrow(
				NotificationNotFoundException::new);
		notification.check();
	}

	@Transactional(readOnly = true)
	public List<NotificationResponse> getUncheckedNotifications(
		Long userId,
		UserType userType
	) {
		return notificationRepository.findAllByNotifiedUserIdAndNotifiedUserTypeAndCheckedFalse(
			userId, userType).stream()
			.map(NotificationResponse::new)
			.collect(Collectors.toList());
	}

}
