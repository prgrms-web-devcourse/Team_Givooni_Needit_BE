package com.prgrms.needit.common.domain.dto;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DealStatusRequest {

	@NotBlank
	private String status;

	public DealStatusRequest(String status) {
		this.status = status;
	}
}
