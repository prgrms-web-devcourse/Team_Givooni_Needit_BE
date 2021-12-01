package com.prgrms.needit.common.response;

import com.prgrms.needit.common.error.ErrorCode;
import lombok.Getter;

@Getter
public class ErrorResponse {

	private int status;
	private String code;
	private String message;

	private ErrorResponse(int status, String code, String message) {
		this.status = status;
		this.code = code;
		this.message = message;
	}

	protected ErrorResponse() {
	}

	public static ErrorResponse of(ErrorCode errorCode) {
		return new ErrorResponse(
			errorCode.getStatus(), errorCode.getCode(), errorCode.getMessage());
	}
}
