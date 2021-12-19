package com.prgrms.needit.domain.user.center.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BusinessesRequest {

	@JsonProperty("businesses")
	private List<BusinessRequest> businesses = new ArrayList<>();

	public BusinessesRequest(BusinessRequest business) {
		this.businesses.add(business);
	}
}
