package com.prgrms.needit.domain.notification.controller;

import com.prgrms.needit.common.enums.UserType;
import com.prgrms.needit.common.error.ErrorCode;
import com.prgrms.needit.common.error.exception.NotFoundResourceException;
import com.prgrms.needit.common.response.ApiResponse;
import com.prgrms.needit.domain.notification.entity.response.NotificationResponse;
import com.prgrms.needit.domain.notification.service.NotificationService;
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

	public Long getCurUserId() {
		if(userService.getCurCenter().isPresent() && userService.getCurMember().isEmpty()) {
			return userService.getCurCenter().get().getId();
		}

		if(userService.getCurMember().isPresent() && userService.getCurCenter().isEmpty()) {
			return userService.getCurMember().get().getId();
		}

		throw new NotFoundResourceException(ErrorCode.NOT_FOUND_USER);
	}

	public UserType getCurUserType() {
		if(userService.getCurCenter().isPresent() && userService.getCurMember().isEmpty()) {
			return UserType.CENTER;
		}

		if(userService.getCurMember().isPresent() && userService.getCurCenter().isEmpty()) {
			return UserType.MEMBER;
		}

		throw new NotFoundResourceException(ErrorCode.NOT_FOUND_USER);
	}

	@GetMapping
	public ResponseEntity<ApiResponse<List<NotificationResponse>>> getMyNotifications() {
		List<NotificationResponse> uncheckedNotifications = notificationService
			.getUncheckedNotifications(getCurUserId(), getCurUserType());
		return ResponseEntity.ok(ApiResponse.of(uncheckedNotifications));
	}

	@DeleteMapping("/{notificationId}")
	public void checkNotification(
		@NotNull @PathVariable("notificationId") Long notificationId
	) {
		notificationService.checkNotification(notificationId);
	}

}
