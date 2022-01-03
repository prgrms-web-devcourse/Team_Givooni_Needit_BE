package com.prgrms.needit.domain.contract.exception;

import com.prgrms.needit.common.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ContractControllerExceptionHandler {

	@ExceptionHandler(IllegalContractStatusException.class)
	public ResponseEntity<ErrorResponse> handleIllegalContractStatus(
		IllegalContractStatusException exception
	) {
		log.error("Illegal contract status occur : {}", exception.getMessage());
		return ResponseEntity
			.status(exception.getHttpStatus())
			.body(ErrorResponse.of(exception));
	}

}
