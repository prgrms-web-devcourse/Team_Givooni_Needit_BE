package com.prgrms.needit.domain.user.center.controller;

import com.prgrms.needit.common.response.ApiResponse;
import com.prgrms.needit.domain.user.center.dto.CenterCreateRequest;
import com.prgrms.needit.domain.user.center.dto.CenterResponse;
import com.prgrms.needit.domain.user.center.dto.CenterUpdateRequest;
import com.prgrms.needit.domain.user.center.service.CenterService;
import com.prgrms.needit.domain.user.user.dto.Response;
import java.io.IOException;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/centers")
public class CenterController {

	private final CenterService centerService;

	public CenterController(CenterService centerService) {
		this.centerService = centerService;
	}

	@PostMapping("/signup")
	public ResponseEntity<ApiResponse<Long>> createCenter(
		@RequestBody CenterCreateRequest request
	) {
		return ResponseEntity.ok(
			ApiResponse.of(centerService.createCenter(request))
		);
	}

	@GetMapping("/search")
	public ResponseEntity<ApiResponse<List<CenterResponse>>> searchCenter(
		@RequestParam @NotBlank String centerName
	) {
		return ResponseEntity.ok(
			ApiResponse.of(centerService.searchCenter(centerName))
		);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<Response.UserInfo>> getOtherCenter(
		@PathVariable Long id
	) {
		return ResponseEntity.ok(
			ApiResponse.of(centerService.getOtherCenter(id))
		);
	}

	@PutMapping
	public ResponseEntity<ApiResponse<Long>> updateCenter(
		@RequestPart(required = false) MultipartFile file,
		@RequestPart @Valid CenterUpdateRequest request
	) throws IOException {
		return ResponseEntity.ok(
			ApiResponse.of(centerService.updateCenter(file, request)));
	}

	@DeleteMapping
	public ResponseEntity<Void> deleteCenter() {
		centerService.deleteCenter();
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
