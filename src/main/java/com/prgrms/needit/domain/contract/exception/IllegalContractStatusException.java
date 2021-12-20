package com.prgrms.needit.domain.contract.exception;

import com.prgrms.needit.common.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class IllegalContractStatusException extends RuntimeException {

	private final ErrorCode errorCode;

}
