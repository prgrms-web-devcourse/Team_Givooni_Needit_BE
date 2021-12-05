package com.prgrms.needit.domain.contract.entity.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.prgrms.needit.domain.contract.entity.Contract;
import com.prgrms.needit.domain.contract.entity.enums.ContractStatus;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class ContractResponse {

	@JsonProperty("contractId")
	private final long id;

	@JsonProperty("contractDate")
	private final LocalDateTime contractDate;

	@JsonProperty("status")
	private final ContractStatus contractStatus;

	public ContractResponse(Contract contract) {
		this.id = contract.getId();
		this.contractDate = contract.getContractDate();
		this.contractStatus = contract.getStatus();
	}

}
