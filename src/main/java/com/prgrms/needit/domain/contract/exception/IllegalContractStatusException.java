package com.prgrms.needit.domain.contract.exception;

import com.prgrms.needit.common.error.ErrorCode;
import com.prgrms.needit.common.error.exception.ErrorCodedException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
public class IllegalContractStatusException extends ErrorCodedException {

	public IllegalContractStatusException(ErrorCode errorCode) {
		super(HttpStatus.FORBIDDEN, errorCode);
	}
}
