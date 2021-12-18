package com.prgrms.needit;

import com.prgrms.needit.common.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sample")
public class SampleController {

	@GetMapping
	public ResponseEntity<ApiResponse<Long>> test() {
		return ResponseEntity.ok(ApiResponse.of(1L));
	}
}

