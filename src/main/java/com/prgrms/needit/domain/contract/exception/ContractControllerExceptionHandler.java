package com.prgrms.needit.domain.contract.exception;

import com.prgrms.needit.common.response.ApiResponse;
import com.prgrms.needit.common.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ContractControllerExceptionHandler {

	@ExceptionHandler(IllegalContractStatusException.class)
	public ResponseEntity<ApiResponse<ErrorResponse>> handleIllegalContractStatus(
		IllegalContractStatusException exception
	) {
		log.error("{} : {}", exception.getClass()
									  .getSimpleName(), exception.getMessage());
		return ResponseEntity.badRequest()
							 .body(ApiResponse.of(ErrorResponse.of(exception.getErrorCode())));
	}

}
