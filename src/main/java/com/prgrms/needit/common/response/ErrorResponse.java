package com.prgrms.needit.common.response;

import com.prgrms.needit.common.error.exception.ErrorCodedException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponse {

	private int status;
	private String code;
	private String message;

	public ErrorResponse(int status, String code, String message) {
		this.status = status;
		this.code = code;
		this.message = message;
	}

	public static ErrorResponse of(ErrorCodedException ex) {
		return new ErrorResponse(
			ex.getHttpStatus()
			  .value(),
			ex.getErrorCode(),
			ex.getMessage()
		);
	}
}
