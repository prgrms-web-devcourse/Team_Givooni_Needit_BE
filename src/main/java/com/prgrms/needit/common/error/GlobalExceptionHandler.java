package com.prgrms.needit.common.error;

import com.prgrms.needit.common.error.exception.MemberNotFoundException;
import com.prgrms.needit.common.error.exception.NotMatchEmailCodeException;
import com.prgrms.needit.common.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MemberNotFoundException.class)
	public ResponseEntity<ErrorResponse> memberNotFoundExceptionHandler(MemberNotFoundException ex) {
		log.error("Exception : " + ex.getMessage());
		ErrorResponse response = ErrorResponse.of(
			ex.getErrorCode()
		);

		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(NotMatchEmailCodeException.class)
	public ResponseEntity<ErrorResponse> notMatchEmailCodeExceptionHandler(
		NotMatchEmailCodeException ex
	) {
		log.error("Exception : " + ex.getMessage());
		ErrorResponse response = ErrorResponse.of(
			ex.getErrorCode()
		);

		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> globalExceptionHandler(Exception ex) {
		log.error("Exception : " + ex.getMessage());
		ErrorResponse response = ErrorResponse.of(
			ErrorCode.INTERNAL_SERVER_ERROR
		);

		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
