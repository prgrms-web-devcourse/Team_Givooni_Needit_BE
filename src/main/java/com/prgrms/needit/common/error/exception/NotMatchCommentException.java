package com.prgrms.needit.common.error.exception;

import com.prgrms.needit.common.error.ErrorCode;
import lombok.Getter;

@Getter
public class NotMatchCommentException extends RuntimeException {

	private ErrorCode errorCode;

	public NotMatchCommentException(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}

}
