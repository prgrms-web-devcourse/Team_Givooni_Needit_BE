package com.prgrms.needit.domain.center.controller;

import com.prgrms.needit.common.response.ApiResponse;
import com.prgrms.needit.domain.center.dto.CenterRequest;
import com.prgrms.needit.domain.center.dto.CenterResponse;
import com.prgrms.needit.domain.center.dto.CenterSelfResponse;
import com.prgrms.needit.domain.center.service.CenterService;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/centers")
public class CenterController {

	private final CenterService centerService;

	public CenterController(CenterService centerService) {
		this.centerService = centerService;
	}

	@PostMapping
	public ResponseEntity<ApiResponse<Long>> createCenter(
		@RequestBody CenterRequest request
	) {
		return ResponseEntity.ok(
			ApiResponse.of(centerService.createCenter(request))
		);
	}

	// TODO: 2021-12-03 Security 적용 후 수정
	@GetMapping
	public ResponseEntity<ApiResponse<CenterSelfResponse>> getCenter() {
		return ResponseEntity.ok(
			ApiResponse.of(centerService.getCenter(1L))
		);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<CenterResponse>> getOtherCenter(
		@PathVariable Long id
	) {
		return ResponseEntity.ok(
			ApiResponse.of(centerService.getOtherCenter(id))
		);
	}

	@PutMapping
	public ResponseEntity<ApiResponse<Long>> updateCenter(
		@RequestBody @Valid CenterRequest request
	) {
		return ResponseEntity.ok(
			ApiResponse.of(centerService.updateCenter(1L, request)));
	}

	@DeleteMapping
	public ResponseEntity<Void> deleteCenter() {
		centerService.deleteCenter(1L);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
