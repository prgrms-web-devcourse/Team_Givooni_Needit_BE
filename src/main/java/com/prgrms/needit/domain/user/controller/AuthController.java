package com.prgrms.needit.domain.user.controller;

import com.prgrms.needit.common.response.ApiResponse;
import com.prgrms.needit.domain.user.dto.Request;
import com.prgrms.needit.domain.user.dto.Response;
import com.prgrms.needit.domain.user.service.AuthService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping("/login")
	public ResponseEntity<ApiResponse<Response.TokenInfo>> login(
		@Valid @RequestBody Request.Login login
	) {
		return ResponseEntity.ok(ApiResponse.of(authService.login(login)));
	}

	@PutMapping("/reissue")
	public ResponseEntity<ApiResponse<Response.TokenInfo>> reissue(
		@Valid @RequestBody Request.Reissue reissue
	) {
		return ResponseEntity.ok(ApiResponse.of(authService.reissue(reissue)));
	}

	@DeleteMapping("/logout")
	public ResponseEntity<Void> logout(
		@Valid @RequestBody Request.Logout logout
	) {
		authService.logout(logout);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
