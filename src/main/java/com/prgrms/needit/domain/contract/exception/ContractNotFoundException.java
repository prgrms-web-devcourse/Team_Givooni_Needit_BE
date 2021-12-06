package com.prgrms.needit.domain.contract.exception;

import lombok.Getter;

@Getter
public class ContractNotFoundException extends RuntimeException {

	public ContractNotFoundException(long contractId) {
		super(String.format("Contract with given id %d not found.", contractId));
	}

}
