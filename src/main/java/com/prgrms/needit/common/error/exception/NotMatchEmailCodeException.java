package com.prgrms.needit.common.error.exception;

import com.prgrms.needit.common.error.ErrorCode;
import lombok.Getter;

@Getter
public class NotMatchEmailCodeException extends RuntimeException {

	private ErrorCode errorCode;

	public NotMatchEmailCodeException(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}
}
