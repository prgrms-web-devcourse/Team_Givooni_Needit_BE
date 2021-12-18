package com.prgrms.needit.domain.user.openAPI.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BusinessRefinedResponse {

	private boolean valid;
	private String validMsg;
	private String status_code;

	public BusinessRefinedResponse(BusinessRawResponse response) {
		this.status_code = response.getStatusCode();
		BusinessRawResponse.Data data = response.getData()
												.get(0); // 한 건 조회
		this.valid = data.getValid()
						 .equals("01");

		this.validMsg = data.getValidMsg();
	}

}
