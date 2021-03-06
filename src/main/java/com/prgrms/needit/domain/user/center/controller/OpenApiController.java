package com.prgrms.needit.domain.user.center.controller;

import com.prgrms.needit.common.response.ApiResponse;
import com.prgrms.needit.domain.user.center.dto.BusinessRefinedResponse;
import com.prgrms.needit.domain.user.center.dto.BusinessRequest;
import com.prgrms.needit.domain.user.center.service.OpenApiService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OpenApiController {

	private final OpenApiService openAPIService;

	@PostMapping("/check-businesscode")
	public ResponseEntity<ApiResponse<BusinessRefinedResponse>> checkBusinesscode(
		@Valid @RequestBody BusinessRequest request
	) {
		return ResponseEntity.ok(
			ApiResponse.of(openAPIService.checkBusinessCode(request)));
	}
}

