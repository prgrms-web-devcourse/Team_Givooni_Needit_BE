package com.prgrms.needit.common.error.exception;

import com.prgrms.needit.common.error.ErrorCode;
import lombok.Getter;

@Getter
public class NotMatchResourceException extends RuntimeException {

	private ErrorCode errorCode;

	public NotMatchResourceException(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}

}
