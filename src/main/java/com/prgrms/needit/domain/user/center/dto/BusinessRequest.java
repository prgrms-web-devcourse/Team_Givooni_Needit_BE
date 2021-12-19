package com.prgrms.needit.domain.user.center.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BusinessRequest {

	@NotBlank
	@JsonProperty("b_no")
	private String registrationCode;

	@NotBlank
	@JsonProperty("start_dt")
	private String startDate;

	@NotBlank
	@JsonProperty("p_nm")
	private String owner;

}