package com.prgrms.needit.domain.contract.controller.bind;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.prgrms.needit.domain.contract.entity.enums.ContractStatus;
import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ContractStatusRequest {

	@NotNull
	@JsonProperty("contractStatus")
	private ContractStatus contractStatus;

}
