package com.prgrms.needit.common.error;

import com.prgrms.needit.common.error.exception.ExistResourceException;
import com.prgrms.needit.common.error.exception.InvalidArgumentException;
import com.prgrms.needit.common.error.exception.NotFoundResourceException;
import com.prgrms.needit.common.error.exception.NotMatchResourceException;
import com.prgrms.needit.common.error.exception.OpenApiException;
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

	@ExceptionHandler(NotMatchResourceException.class)
	public ResponseEntity<ErrorResponse> NotMatchResourceExceptionHandler(NotMatchResourceException ex) {
		log.error("Exception: {}", ex.getMessage());
		ErrorResponse response = ErrorResponse.of(
			ex.getErrorCode()
		);

		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
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

	@ExceptionHandler(OpenApiException.class)
	public ResponseEntity<ErrorResponse> OpenApiServerExceptionHandler(OpenApiException ex) {
		log.error("Exception: {}", ex.getMessage());
		ErrorResponse response = ErrorResponse.of(
			ex.getErrorCode()
		);

		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(ExistResourceException.class)
	public ResponseEntity<ErrorResponse> ExistResourceExceptionHandler(ExistResourceException ex) {
		log.error("Exception: {}", ex.getMessage());
		ErrorResponse response = ErrorResponse.of(
			ex.getErrorCode()
		);

		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

}
