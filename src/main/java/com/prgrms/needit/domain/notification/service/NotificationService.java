package com.prgrms.needit.domain.notification.service;

import com.prgrms.needit.domain.notification.entity.Notification;
import com.prgrms.needit.domain.notification.entity.response.NotificationResponse;
import com.prgrms.needit.domain.notification.repository.NotificationRepository;
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

	public NotificationResponse createAndSendNotification(
		String username,
		String content,
		String link
	) {
		Notification notification = Notification
			.builder()
			.username(username)
			.notificationContent(content)
			.accessLink(link)
			.build();
		messagingTemplate.convertAndSendToUser(
			username,
			"/topic/notifications",
			new NotificationResponse(notification));
		return new NotificationResponse(notificationRepository.save(notification));
	}

}
