package com.prgrms.needit.domain.user.openAPI.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class BusinessRefinedResponse {

	@JsonProperty("valid")
	private boolean valid;

	@JsonProperty("valid_msg")
	private String valid_msg;

	@JsonProperty("status_code")
	private String status_code;

	public BusinessRefinedResponse(BusinessRawResponse response) {
		this.status_code = response.getStatus_code();
		BusinessRawResponse.Data data = response.getData()
												.get(0); // 한 건 조회
		this.valid = data.getValid()
						 .equals("01");
		this.valid_msg = data.getValid_msg();
	}

}
