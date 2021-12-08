package com.prgrms.needit.common.email;


import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class EmailController {

	private final EmailService emailService;

	@PostMapping("/email") // 이메일 인증 코드 보내기
	public ResponseEntity<String> emailAuth(@RequestBody Map<String, String> email)
		throws Exception {
		emailService.sendMessage(email.get("email"));
		return ResponseEntity.ok("인증코드 전송 완료");
	}

	@PutMapping("/email")
	public ResponseEntity<String> resendEmail(@RequestBody Map<String, String> email)
		throws Exception {
		emailService.resendMessage(email.get("email"));
		return ResponseEntity.ok("인증코드 재전송 완료");
	}

	@PostMapping("/verifyCode") // 이메일 인증 코드 검증
	public ResponseEntity<String> verifyCode(@RequestBody Map<String, String> request) {
		emailService.verifyCode(request.get("email"), request.get("code"));
		return ResponseEntity.ok("인증코드 검증 완료");
	}
}