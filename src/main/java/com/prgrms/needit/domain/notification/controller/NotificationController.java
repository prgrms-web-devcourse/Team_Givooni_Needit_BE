package com.prgrms.needit.domain.notification.controller;

import com.prgrms.needit.common.response.ApiResponse;
import com.prgrms.needit.domain.notification.entity.response.NotificationResponse;
import com.prgrms.needit.domain.notification.service.NotificationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/notification")
public class NotificationController {

	private final NotificationService notificationService;

	@GetMapping
	public ResponseEntity<ApiResponse<List<NotificationResponse>>> getMyNotifications(
		@AuthenticationPrincipal Object authenticatedUser
	) {
		// TODO: get authority from authentication principal and conditionally call.
		List<NotificationResponse> uncheckedNotifications = notificationService
			.getUncheckedNotifications(null, null);
		return ResponseEntity.ok(ApiResponse.of(uncheckedNotifications));
	}

	@DeleteMapping("/{notificationId}")
	public void checkNotification(
		@PathVariable("notificationId") long notificationId
	) {
		notificationService.checkNotification(notificationId);
	}

}
