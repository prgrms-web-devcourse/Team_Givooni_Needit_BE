package com.prgrms.needit.common.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "C001", "정의되지 않은 에러가 발생했습니다."),
	INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST.value(), "C002", "올바른 입력 형식이 아닙니다."),
        
	NOT_FOUND_CONTRACT(HttpStatus.NOT_FOUND.value(), "G001", "해당 기부 예약을 찾을 수 없습니다."),
	NOT_SUPPORTED_STATUS(HttpStatus.BAD_REQUEST.value(), "G002", "적용할 수 없는 예약 상태입니다.");

	private final int status;
	private final String code;
	private final String message;

	ErrorCode(int status, String code, String message) {
		this.code = code;
		this.message = message;
		this.status = status;
	}
}
