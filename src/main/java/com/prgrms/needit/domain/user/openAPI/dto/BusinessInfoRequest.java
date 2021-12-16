package com.prgrms.needit.domain.user.openAPI.dto;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotBlank;

public class BusinessInfoRequest {

	@NotBlank
	private Long b_no;

	@NotBlank
	private Long start_dt;

	@NotBlank
	private String p_nm;


	public BusinessInfoRequest(Long b_no, Long start_dt, String p_nm) {
		this.b_no = b_no;
		this.start_dt = start_dt;
		this.p_nm = p_nm;
	}

	public BusinessesRequest toRequest() {
		List<BusinessInfoRequest> array = new ArrayList<>();
		array.add(this);
		return new BusinessesRequest(array);
	}
}
