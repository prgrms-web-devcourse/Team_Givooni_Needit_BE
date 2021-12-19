package com.prgrms.needit.domain.user.center.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BusinessRefinedResponse {

	private boolean valid;
	private String validMsg;
	private String statusCode;

	public BusinessRefinedResponse(BusinessRawResponse response) {
		this.statusCode = response.getStatusCode();
		BusinessRawResponse.Data data = response.getData()
												.get(0); // 한 건 조회
		this.valid = data.getValid()
						 .equals("01");

		this.validMsg = data.getValidMsg();
	}

	public BusinessRefinedResponse(boolean valid, String validMsg, String statusCode) {
		this.valid = valid;
		this.validMsg = validMsg;
		this.statusCode = statusCode;
	}

}
