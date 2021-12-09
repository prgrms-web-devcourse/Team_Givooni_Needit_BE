package com.prgrms.needit.domain.notification.exception;

import com.prgrms.needit.common.error.ErrorCode;
import com.prgrms.needit.common.error.exception.CustomException;
import lombok.Getter;

@Getter
public class NotificationNotFoundException extends CustomException {
	public NotificationNotFoundException() {
		super(ErrorCode.NOT_FOUND_NOTIFICATION);
	}
}
