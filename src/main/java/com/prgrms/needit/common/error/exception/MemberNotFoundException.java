package com.prgrms.needit.common.error.exception;

import com.prgrms.needit.common.error.ErrorCode;
import lombok.Getter;

@Getter
public class MemberNotFoundException extends RuntimeException {

	private ErrorCode errorCode;

	public MemberNotFoundException(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}
}
