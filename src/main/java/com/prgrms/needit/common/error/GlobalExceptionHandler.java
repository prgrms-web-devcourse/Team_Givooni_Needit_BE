package com.prgrms.needit.common.error;

import com.prgrms.needit.common.error.exception.ExistResourceException;
import com.prgrms.needit.common.error.exception.InvalidArgumentException;
import com.prgrms.needit.common.error.exception.NotFoundResourceException;
import com.prgrms.needit.common.error.exception.NotMatchResourceException;
import com.prgrms.needit.common.error.exception.OpenApiException;
import com.prgrms.needit.common.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(InvalidArgumentException.class)
	public ResponseEntity<ErrorResponse> invalidArgumentExceptionHandler(InvalidArgumentException ex) {
		ErrorCode errorCode = ex.getErrorCode();
		log.error("InvalidArgumentException: {}", errorCode.getMessage());

		return ResponseEntity.status(errorCode.getStatus())
							 .body(ErrorResponse.of(errorCode));
	}

	@ExceptionHandler(NotFoundResourceException.class)
	public ResponseEntity<ErrorResponse> notFoundResourceExceptionHandler(NotFoundResourceException ex) {
		ErrorCode errorCode = ex.getErrorCode();
		log.error("NotFoundResourceException: {}", errorCode.getMessage());

		return ResponseEntity.status(errorCode.getStatus())
							 .body(ErrorResponse.of(errorCode));
	}

	@ExceptionHandler(NotMatchResourceException.class)
	public ResponseEntity<ErrorResponse> notMatchResourceExceptionHandler(NotMatchResourceException ex) {
		ErrorCode errorCode = ex.getErrorCode();
		log.error("NotMatchResourceException: {}", errorCode.getMessage());

		return ResponseEntity.status(errorCode.getStatus())
							 .body(ErrorResponse.of(errorCode));
	}

	@ExceptionHandler(MailException.class)
	public ResponseEntity<ErrorResponse> mailSendFailedExceptionHandler(MailException ex) {
		log.error("MailException: {}", ex.getMessage());
		ErrorResponse response = ErrorResponse.of(
			ErrorCode.MAIL_SEND_FAILED
		);

		return ResponseEntity.status(response.getStatus())
							 .body(response);
	}

	@ExceptionHandler(OpenApiException.class)
	public ResponseEntity<ErrorResponse> openApiServerExceptionHandler(OpenApiException ex) {
		ErrorCode errorCode = ex.getErrorCode();
		log.error("OpenApiException: {}", errorCode.getMessage());

		return ResponseEntity.status(errorCode.getStatus())
							 .body(ErrorResponse.of(errorCode));
	}

	@ExceptionHandler(ExistResourceException.class)
	public ResponseEntity<ErrorResponse> existResourceExceptionHandler(ExistResourceException ex) {
		ErrorCode errorCode = ex.getErrorCode();
		log.error("ExistResourceException: {}", errorCode.getMessage());

		return ResponseEntity.status(errorCode.getStatus())
							 .body(ErrorResponse.of(errorCode));
	}

}
