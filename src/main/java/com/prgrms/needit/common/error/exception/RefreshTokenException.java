package com.prgrms.needit.common.error.exception;

import com.prgrms.needit.common.error.ErrorCode;
import org.springframework.http.HttpStatus;

public class RefreshTokenException extends ErrorCodedException {

	public RefreshTokenException(ErrorCode errorCode) {
		super(HttpStatus.BAD_REQUEST, errorCode);
	}
}
