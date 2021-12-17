package com.prgrms.needit.domain.user.openAPI.controller;

import com.prgrms.needit.common.response.ApiResponse;
import com.prgrms.needit.domain.user.openAPI.dto.BusinessRefinedResponse;
import com.prgrms.needit.domain.user.openAPI.dto.BusinessRequest;
import com.prgrms.needit.domain.user.openAPI.service.OpenApiService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OpenApiController {

	private final OpenApiService openAPIService;

	@PostMapping("/check-businesscode")
	public ApiResponse<BusinessRefinedResponse> checkBusinesscode(
		@Valid @RequestBody BusinessRequest request
	) {
		return ApiResponse.of(openAPIService.checkBusinesscode(request));
	}
}

