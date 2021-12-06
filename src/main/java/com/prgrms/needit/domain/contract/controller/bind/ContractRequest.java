package com.prgrms.needit.domain.contract.controller.bind;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ContractRequest {

	@JsonProperty("contractDate")
	@NotNull
	private LocalDateTime contractDate;

}
