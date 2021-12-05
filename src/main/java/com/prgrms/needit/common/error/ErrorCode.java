package com.prgrms.needit.common.error;

import lombok.Getter;

@Getter
public enum ErrorCode {
	INTERNAL_SERVER_ERROR(500, "C001", "정의되지 않은 에러가 발생했습니다."),
	INVALID_INPUT_VALUE(400, "C002", "올바른 입력 형식이 아닙니다."),
	NOT_MATCH_WRITER(400, "D003", "게시글의 작성자가 아닙니다."),

	NOT_FOUND_DONATION(404, "D001", "존재하지 않는 기부글입니다."),
	INVALID_CATEGORY_VALUE(400, "D002", "잘못된 카테고리 타입입니다."),
	INVALID_QUALITY_VALUE(400, "D003", "잘못된 품질상태 타입입니다."),
	INVALID_STATUS_VALUE(400, "D004", "잘못된 거래상태 타입입니다.");

	private final String code;
	private final String message;
	private int status;

	ErrorCode(int status, String code, String message) {
		this.code = code;
		this.message = message;
		this.status = status;
	}
}
