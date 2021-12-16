package com.prgrms.needit.common.error.exception;

import com.prgrms.needit.common.error.ErrorCode;
import lombok.Getter;

@Getter
public class OpenApiException extends RuntimeException {

	private ErrorCode errorCode;

	public OpenApiException(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}
}