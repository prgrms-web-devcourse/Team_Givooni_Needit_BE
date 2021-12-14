package com.prgrms.needit.domain.notification.controller;

import com.prgrms.needit.common.enums.UserType;
import com.prgrms.needit.common.response.ApiResponse;
import com.prgrms.needit.domain.notification.entity.response.NotificationResponse;
import com.prgrms.needit.domain.notification.service.NotificationService;
import com.prgrms.needit.domain.user.login.dto.CurUser;
import com.prgrms.needit.domain.user.login.service.UserService;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
	private final UserService userService;

	@GetMapping
	public ResponseEntity<ApiResponse<List<NotificationResponse>>> getMyNotifications() {
		CurUser curUser = userService.getCurUser();
		List<NotificationResponse> uncheckedNotifications = notificationService
			.getUncheckedNotifications(curUser.getId(), UserType.valueOf(curUser.getRole()));
		return ResponseEntity.ok(ApiResponse.of(uncheckedNotifications));
	}

	@DeleteMapping("/{notificationId}")
	public void checkNotification(
		@NotNull @PathVariable("notificationId") Long notificationId
	) {
		notificationService.checkNotification(notificationId);
	}

}
