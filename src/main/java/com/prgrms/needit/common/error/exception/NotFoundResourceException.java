package com.prgrms.needit.common.error.exception;

import com.prgrms.needit.common.error.ErrorCode;
import lombok.Getter;

@Getter
public class NotFoundResourceException extends RuntimeException {

	private ErrorCode errorCode;

	public NotFoundResourceException(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}
}
