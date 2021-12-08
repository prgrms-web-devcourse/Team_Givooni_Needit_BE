package com.prgrms.needit.domain.contract.controller.bind;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.prgrms.needit.common.enums.BoardType;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ContractRequest {

	@NotNull
	@JsonProperty("boardType")
	private BoardType boardType;

	@NotNull
	@JsonProperty("commentId")
	private Long commentId;

	@NotNull
	@JsonProperty("contractDate")
	private LocalDateTime contractDate;

}
