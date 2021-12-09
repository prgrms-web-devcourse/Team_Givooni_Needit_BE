package com.prgrms.needit.common.email;


import java.util.Map;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmailController {

	private final EmailService emailService;

	@PostMapping("/email") // 이메일 인증 코드 보내기
	public ResponseEntity<String> emailAuth(
		@RequestBody @Valid Map<String, String> email
	) throws Exception {
		emailService.sendMessage(email.get("email"));
		return ResponseEntity.ok("인증코드 전송 완료");
	}

	@PutMapping("/email") // 이메일 인증 코드 재전송
	public ResponseEntity<String> resendEmail(
		@RequestBody @Valid Map<String, String> email
	) throws Exception {
		emailService.resendMessage(email.get("email"));
		return ResponseEntity.ok("인증코드 재전송 완료");
	}

	@PostMapping("/verifyCode") // 이메일 인증 코드 검증
	public ResponseEntity<String> verifyCode(
		@RequestBody @Valid Map<String, String> request
	) {
		emailService.verifyCode(request.get("email"), request.get("code"));
		return ResponseEntity.ok("인증코드 검증 완료");
	}
}