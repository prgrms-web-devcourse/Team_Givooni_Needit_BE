package com.prgrms.needit.domain.user.openAPI.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BusinessRequest {

	@JsonProperty("b_no")
	private String b_no;
	@JsonProperty("start_dt")
	private String start_dt;
	@JsonProperty("p_nm")
	private String p_nm;

}