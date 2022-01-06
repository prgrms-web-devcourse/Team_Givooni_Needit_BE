package com.prgrms.needit.common.error.exception;

import com.prgrms.needit.common.error.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class OpenApiException extends ErrorCodedException {

	public OpenApiException(ErrorCode errorCode) {
		super(HttpStatus.INTERNAL_SERVER_ERROR, errorCode);
	}
}