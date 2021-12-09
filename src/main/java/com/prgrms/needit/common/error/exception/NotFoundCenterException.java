package com.prgrms.needit.common.error.exception;

import com.prgrms.needit.common.error.ErrorCode;
import lombok.Getter;

@Getter
public class NotFoundCenterException extends RuntimeException {

	private ErrorCode errorCode;

	public NotFoundCenterException(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}
}
