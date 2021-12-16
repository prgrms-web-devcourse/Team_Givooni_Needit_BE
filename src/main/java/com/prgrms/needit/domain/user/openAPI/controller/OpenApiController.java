package com.prgrms.needit.domain.user.openAPI.controller;

import com.prgrms.needit.domain.user.openAPI.service.OpenApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OpenApiController {

	private final OpenApiService openAPIService;

	@PostMapping("/callApi")
	public String callApi() {
		return openAPIService.callApi();
	}
}

