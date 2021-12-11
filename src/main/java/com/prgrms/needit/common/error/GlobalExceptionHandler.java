package com.prgrms.needit.common.error;

import com.prgrms.needit.common.error.exception.InvalidArgumentException;
import com.prgrms.needit.common.error.exception.NotFoundResourceException;
import com.prgrms.needit.common.error.exception.NotMatchCommentException;
import com.prgrms.needit.common.error.exception.NotMatchEmailCodeException;
import com.prgrms.needit.common.error.exception.NotMatchWriterException;
import com.prgrms.needit.common.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(InvalidArgumentException.class)
	public ResponseEntity<ErrorResponse> InvalidArgumentExceptionHandler(InvalidArgumentException ex) {
		log.error("Exception: {}", ex.getMessage());
		ErrorResponse response = ErrorResponse.of(
			ex.getErrorCode()
		);

		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(NotFoundResourceException.class)
	public ResponseEntity<ErrorResponse> NotFoundResourceExceptionHandler(NotFoundResourceException ex) {
		log.error("Exception: {}", ex.getMessage());
		ErrorResponse response = ErrorResponse.of(
			ex.getErrorCode()
		);

		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(NotMatchWriterException.class)
	public ResponseEntity<ErrorResponse> NotMatchWriterExceptionHandler(NotMatchWriterException ex) {
		log.error("Exception: {}", ex.getMessage());
		ErrorResponse response = ErrorResponse.of(
			ex.getErrorCode()
		);

		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(NotMatchCommentException.class)
	public ResponseEntity<ErrorResponse> NotMatchCommentExceptionHandler(NotMatchCommentException ex) {
		log.error("Exception: {}", ex.getMessage());
		ErrorResponse response = ErrorResponse.of(
			ex.getErrorCode()
		);

		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(NotMatchEmailCodeException.class)
	public ResponseEntity<ErrorResponse> NotMatchEmailCodeExceptionHandler(
		NotMatchEmailCodeException ex
	) {
		log.error("Exception: {}", ex.getMessage());
		ErrorResponse response = ErrorResponse.of(
			ex.getErrorCode()
		);

		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MailException.class)
	public ResponseEntity<ErrorResponse> MailSendFailedExceptionHandler(
		MailException ex
	) {
		log.error("Exception: {}", ex.getMessage());
		ErrorResponse response = ErrorResponse.of(
			ErrorCode.MAIL_SEND_FAILED
		);

		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> globalExceptionHandler(Exception ex) {
		log.error("Exception: {}", ex.getMessage());
		ErrorResponse response = ErrorResponse.of(
			ErrorCode.INTERNAL_SERVER_ERROR
		);

		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
