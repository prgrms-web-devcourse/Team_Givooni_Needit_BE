package com.prgrms.needit.common.error.exception;

import com.prgrms.needit.common.error.ErrorCode;
import lombok.Getter;

@Getter
public class InvalidArgumentException extends RuntimeException {

	private ErrorCode errorCode;

	public InvalidArgumentException(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}

}

