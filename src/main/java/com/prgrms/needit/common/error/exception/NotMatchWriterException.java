package com.prgrms.needit.common.error.exception;

import com.prgrms.needit.common.error.ErrorCode;
import lombok.Getter;

@Getter
public class NotMatchWriterException extends RuntimeException {

	private ErrorCode errorCode;

	public NotMatchWriterException(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}
}

