package com.prgrms.needit.common.error;

import lombok.Getter;

@Getter
public enum ErrorCode {
	INTERNAL_SERVER_ERROR(500, "G001", "정의되지 않은 에러가 발생했습니다."),
	INVALID_INPUT_VALUE(400, "G002", "올바른 입력 형식이 아닙니다."),
	NOT_FOUND_USER(400, "G003", "존재하지 않는 사용자입니다."),
	NOT_MATCH_WRITER(400, "G004", "게시글의 작성자가 아닙니다."),
	NOT_MATCH_COMMENT(400, "G005", "게시글의 댓글이 아닙니다."),

	NOT_FOUND_DONATION(404, "D001", "존재하지 않는 기부글입니다."),
	INVALID_CATEGORY_VALUE(400, "D002", "잘못된 카테고리 타입입니다."),
	INVALID_QUALITY_VALUE(400, "D003", "잘못된 품질상태 타입입니다."),
	INVALID_STATUS_VALUE(400, "D004", "잘못된 거래상태 타입입니다."),
	NOT_FOUND_WISH_COMMENT(404, "D005", "존재하지 않는 기부희망댓글입니다."),

	NOT_FOUND_DONATION_WISH(404, "D001", "존재하지 않는 기부희망글입니다."),

	NOT_FOUND_MEMBER(404, "M001", "존재하지 않는 회원입니다."),

	NOT_MATCH_EMAIL_CODE(400, "E001", "발송된 인증 코드가 아닙니다.");

	private final int status;
	private final String code;
	private final String message;

	ErrorCode(int status, String code, String message) {
		this.code = code;
		this.message = message;
		this.status = status;
	}
}
