package com.prgrms.needit.domain.notification.service;

import com.prgrms.needit.common.enums.UserType;
import com.prgrms.needit.common.error.ErrorCode;
import com.prgrms.needit.common.error.exception.NotFoundResourceException;
import com.prgrms.needit.domain.message.repository.ChatMessageRepository;
import com.prgrms.needit.domain.notification.entity.Notification;
import com.prgrms.needit.domain.notification.entity.enums.NotificationContentType;
import com.prgrms.needit.domain.notification.entity.response.NotificationResponse;
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

	private final ChatMessageRepository chatMessageRepository;
	private final NotificationRepository notificationRepository;
	private final SimpMessagingTemplate messagingTemplate;

	// TODO: CREATE chat notification records and send.
	public void sendChatNotification(
		String receiverUsername,
		Object chatMessage) {
		messagingTemplate.convertAndSendToUser(
			receiverUsername,
			"/topic/chats",
			chatMessage);
	}

	public void createAndSendNotification(
		String username,
		Long userId,
		UserType userType,
		NotificationContentType notificationContentType,
		Long notificationContentValue,
		String previewMessage
	) {
		Notification notification = notificationRepository.save(
			Notification
				.builder()
				.userId(userId)
				.userType(userType)
				.contentType(notificationContentType)
				.contentId(notificationContentValue)
				.previewMessage(previewMessage)
				.build()
		);

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
			.orElseThrow(() -> new NotFoundResourceException(ErrorCode.NOT_FOUND_NOTIFICATION));
		notification.check();
	}

	@Transactional(readOnly = true)
	public List<NotificationResponse> getUncheckedNotifications(
		Long userId,
		UserType userType
	) {
		return notificationRepository.findAllByUserIdAndUserTypeAndCheckedFalse(
										 userId, userType)
									 .stream()
									 .map(NotificationResponse::new)
									 .collect(Collectors.toList());
	}

}
