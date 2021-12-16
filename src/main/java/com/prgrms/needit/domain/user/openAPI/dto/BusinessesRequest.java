package com.prgrms.needit.domain.user.openAPI.dto;

import java.util.List;
import javax.validation.constraints.NotBlank;

public class BusinessesRequest {

	@NotBlank
	private List<BusinessInfoRequest> businesses;

	public BusinessesRequest(List<BusinessInfoRequest> businesses) {
		this.businesses = businesses;
	}

}
