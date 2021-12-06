package com.prgrms.needit.domain.contract.exception;

import com.prgrms.needit.common.error.ErrorCode;
import com.prgrms.needit.common.error.exception.CustomException;
import lombok.Getter;

@Getter
public class IllegalContractStatusException extends CustomException {

	public IllegalContractStatusException(ErrorCode errorCode) {
		super(errorCode);
	}

}
