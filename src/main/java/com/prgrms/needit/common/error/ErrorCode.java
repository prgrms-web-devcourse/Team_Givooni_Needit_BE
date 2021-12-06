package com.prgrms.needit.common.error;

import lombok.Getter;

@Getter
public enum ErrorCode {
        INTERNAL_SERVER_ERROR(500, "C001", "정의되지 않은 에러가 발생했습니다."),
        INVALID_INPUT_VALUE(400, "C002", "올바른 입력 형식이 아닙니다."),
        
	NOT_FOUND_CONTRACT(404, "???", "해당 기부 예약을 찾을 수 없습니다."); // message source 적용?

	private final String code;
	private final String message;
	private int status;

	ErrorCode(int status, String code, String message) {
		this.code = code;
		this.message = message;
		this.status = status;
	}
}
