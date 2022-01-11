package com.prgrms.needit.domain.user.user.controller;

import com.prgrms.needit.common.response.ApiResponse;
import com.prgrms.needit.domain.user.user.dto.Request;
import com.prgrms.needit.domain.user.user.dto.Response;
import com.prgrms.needit.domain.user.user.service.UserService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@PostMapping("/login")
	public ResponseEntity<ApiResponse<Response.TokenInfo>> login(
		@Valid @RequestBody Request.Login login
	) {
		return ResponseEntity.ok(ApiResponse.of(userService.login(login)));
	}

	@PutMapping("/reissue")
	public ResponseEntity<ApiResponse<Response.TokenInfo>> reissue(
	@Valid @RequestBody Request.Reissue reissue
	) {
		return ResponseEntity.ok(ApiResponse.of(userService.reissue(reissue)));
	}

	@DeleteMapping("/logout")
	public ResponseEntity<Void> logout(
		@Valid @RequestBody Request.Logout logout
	) {
		userService.logout(logout);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@PostMapping("/check-email")
	public ResponseEntity<ApiResponse<Response.IsUnique>> checkEmail(
		@Valid @RequestBody Request.IsUniqueEmail request
	) {
		return ResponseEntity.ok(
			ApiResponse.of(userService.isEmailUnique(request))
		);
	}

	@PostMapping("/check-nickname")
	public ResponseEntity<ApiResponse<Response.IsUnique>> checkNickname(
		@Valid @RequestBody Request.IsUniqueNickname request
	) {
		return ResponseEntity.ok(
			ApiResponse.of(userService.isNicknameUnique(request))
		);
	}

	@GetMapping
	public ResponseEntity<ApiResponse<Response.UserInfo>> getCurUser() {
		return ResponseEntity.ok(ApiResponse.of(userService.getUserInfo()));
	}

}
