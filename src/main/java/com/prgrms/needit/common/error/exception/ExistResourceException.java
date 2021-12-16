package com.prgrms.needit.common.error.exception;

import com.prgrms.needit.common.error.ErrorCode;
import lombok.Getter;

@Getter
public class ExistResourceException extends RuntimeException {

	private ErrorCode errorCode;

	public ExistResourceException(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}
}