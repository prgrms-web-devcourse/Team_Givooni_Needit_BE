package com.prgrms.needit.common.error.exception;

import com.prgrms.needit.common.error.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NotMatchResourceException extends ErrorCodedException {

	public NotMatchResourceException(ErrorCode errorCode) {
		super(HttpStatus.FORBIDDEN, errorCode);
	}

}
