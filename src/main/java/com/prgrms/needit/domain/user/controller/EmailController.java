package com.prgrms.needit.domain.user.controller;

import com.prgrms.needit.common.response.ApiResponse;
import com.prgrms.needit.domain.user.dto.EmailCodeRequest;
import com.prgrms.needit.domain.user.dto.EmailRequest;
import com.prgrms.needit.domain.user.service.EmailService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmailController {

	private final EmailService emailService;

	@PostMapping("/email")
	public ResponseEntity<ApiResponse<String>> emailAuth(
		@RequestBody @Valid EmailRequest request
	) {
		emailService.sendMessage(request.getEmail());
		return ResponseEntity.ok(ApiResponse.of("인증코드 전송 완료"));
	}

	@PostMapping("/verify-code")
	public ResponseEntity<ApiResponse<String>> verifyCode(
		@RequestBody @Valid EmailCodeRequest request
	) {
		emailService.verifyCode(request.getEmail(), request.getCode());
		return ResponseEntity.ok(ApiResponse.of("인증코드 검증 완료"));
	}
}