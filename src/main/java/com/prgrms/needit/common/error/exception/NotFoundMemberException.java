package com.prgrms.needit.common.error.exception;

import com.prgrms.needit.common.error.ErrorCode;
import lombok.Getter;

@Getter
public class NotFoundMemberException extends RuntimeException {

	private ErrorCode errorCode;

	public NotFoundMemberException(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}
}
