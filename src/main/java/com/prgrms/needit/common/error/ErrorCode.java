package com.prgrms.needit.common.error;

import lombok.Getter;

@Getter
public enum ErrorCode {
	INTERNAL_SERVER_ERROR(500, "C001", "정의되지 않은 에러가 발생했습니다."),
	INVALID_INPUT_VALUE(400, "C002", "올바른 입력 형식이 아닙니다."),

	NOT_FOUND_MEMBER(404, "M001", "존재하지 않는 회원입니다."),

	NOT_MATCH_EMAIL_CODE(400, "E001", "발송된 인증 코드가 아닙니다.");

	private final String code;
	private final String message;
	private int status;

	ErrorCode(int status, String code, String message) {
		this.code = code;
		this.message = message;
		this.status = status;
	}
}
