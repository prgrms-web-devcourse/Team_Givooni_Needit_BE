package com.prgrms.needit.email;


import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class EmailController {

	private final EmailService emailService;

	@PostMapping("/email") // 이메일 인증 코드 보내기
	public ResponseEntity<String> emailAuth(@RequestBody Map<String, String> email)
		throws Exception {
		emailService.sendSimpleMessage(email.get("email"));
		return new ResponseEntity<>("인증코드 전송완료", HttpStatus.OK);
	}

	@PostMapping("/verifyCode") // 이메일 인증 코드 검증
	public ResponseEntity<String> verifyCode(@RequestBody Map<String, String> code) {
		emailService.verifyCode(code.get("code"));
		return new ResponseEntity<>("인증코드 검증완료", HttpStatus.OK);
	}
}