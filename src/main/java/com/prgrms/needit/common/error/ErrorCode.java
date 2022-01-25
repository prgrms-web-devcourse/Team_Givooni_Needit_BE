package com.prgrms.needit.common.error;

import lombok.Getter;

@Getter
public enum ErrorCode {
	INTERNAL_SERVER_ERROR("G001", "정의되지 않은 에러가 발생했습니다."),
	INVALID_INPUT_VALUE("G002", "올바른 입력 형식이 아닙니다."),
	NOT_FOUND_USER("G003", "존재하지 않는 사용자입니다."),
	NOT_MATCH_WRITER("G004", "게시글의 작성자가 아닙니다."),
	NOT_MATCH_COMMENT("G005", "게시글의 댓글이 아닙니다."),

	INVALID_USER_ROLE_VALUE("U001", "올바르지 않은 사용자 권한입니다."),
	NOT_FOUND_MEMBER("M001", "존재하지 않는 회원입니다."),
	NOT_FOUND_CENTER("C001", "존재하지 않는 센터입니다."),

	NOT_FOUND_DONATION("D001", "존재하지 않는 기부글입니다."),
	NOT_FOUND_DONATION_WISH("D002", "존재하지 않는 기부희망글입니다."),
	INVALID_CATEGORY_VALUE("D003", "잘못된 카테고리 타입입니다."),
	INVALID_QUALITY_VALUE("D004", "잘못된 품질상태 타입입니다."),
	INVALID_STATUS_VALUE("D005", "잘못된 거래상태 타입입니다."),
	NOT_FOUND_WISH_COMMENT("D006", "존재하지 않는 기부희망댓글입니다."),
	NOT_FOUND_APPLY_COMMENT("D007", "존재하지 않는 기부신청댓글입니다."),
	INVALID_STATUS_CHANGE("D008", "허가되지 않은 거래상태 변경입니다."),
	ALREADY_EXIST_COMMENT("D009", "이미 댓글을 등록하였습니다."),

	NOT_FOUND_CONTRACT("R001", "존재하지 않는 기부예약입니다."),
	NOT_SUPPORTED_STATUS("R002", "적용할 수 없는 예약 상태입니다."),
	INVALID_BOARD_TYPE("R003", "잘못된 게시글 타입입니다."),

	NOT_FOUND_NOTIFICATION("N001", "존재하지 않는 알림입니다."),

	NOT_MATCH_EMAIL_CODE("E001", "발송된 인증 코드가 아닙니다."),
	MAIL_SEND_FAILED("E002", "이메일 전송에 실패했습니다."),
	INVALID_EMAIL("E003", "이메일 또는 인증코드가 유효하지 않습니다."),

	OPENAPI_CONN_FAIL("O001", "공공 api 연결에 실패했습니다."),
	ALREADY_EXIST_OPENAPI_CONN("O002", "이미 공공 api 서버에 연결돼있습니다."),
	UNSUPPORTED_ENCODING("O003", "지원하지 않는 형식으로 인코딩돼있습니다."),

	UNAUTHORIZED_CHAT_DIRECTION("H001", "발송 권한이 없는 쪽지입니다."),

	ALREADY_EXIST_FAVCENTER("F001", "이미 존재하는 관심센터입니다."),

	NOT_FOUND_ACTIVITY("A001", "존재하지 않는 활동 게시글입니다."),
	NOT_FOUND_ACTIVITY_COMMENT("A002", "존재하지 않는 활동 게시글 댓글입니다."),

	INVALID_REFRESH_TOKEN("R001", "Refresh Token 정보가 유효하지 않습니다."),
	NOT_FOUND_REFRESH_TOKEN("R002", "존재하지 않는 Refresh Token 입니다."),
	NOT_MATCH_REFRESH_TOKEN("R003", "Refresh Token 정보가 일치하지 않습니다.");

	private final String code;
	private final String message;

	ErrorCode(String code, String message) {
		this.code = code;
		this.message = message;
	}
}