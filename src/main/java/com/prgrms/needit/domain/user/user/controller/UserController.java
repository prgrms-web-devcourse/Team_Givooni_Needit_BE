package com.prgrms.needit.domain.user.user.controller;

import com.prgrms.needit.common.response.ApiResponse;
import com.prgrms.needit.domain.user.user.dto.IsUniqueRequest;
import com.prgrms.needit.domain.user.user.dto.IsUniqueResponse;
import com.prgrms.needit.domain.user.user.dto.LoginRequest;
import com.prgrms.needit.domain.user.user.dto.LogoutRequest;
import com.prgrms.needit.domain.user.user.dto.ReissueRequest;
import com.prgrms.needit.domain.user.user.dto.TokenResponse;
import com.prgrms.needit.domain.user.user.dto.UserResponse;
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
	public ResponseEntity<ApiResponse<TokenResponse>> login(
		@Valid @RequestBody LoginRequest login
	) {
		return ResponseEntity.ok(ApiResponse.of(userService.login(login)));
	}

	@PutMapping("/reissue")
	public ResponseEntity<ApiResponse<TokenResponse>> reissue(
	@Valid @RequestBody ReissueRequest reissue
	) {
		return ResponseEntity.ok(ApiResponse.of(userService.reissue(reissue)));
	}

	@DeleteMapping("/logout")
	public ResponseEntity<Void> logout(
		@Valid @RequestBody LogoutRequest logout
	) {
		userService.logout(logout);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@PostMapping("/check-email")
	public ResponseEntity<ApiResponse<IsUniqueResponse>> checkEmail(
		@Valid @RequestBody IsUniqueRequest.Email request
	) {
		return ResponseEntity.ok(
			ApiResponse.of(userService.isEmailUnique(request))
		);
	}

	@PostMapping("/check-nickname")
	public ResponseEntity<ApiResponse<IsUniqueResponse>> checkNickname(
		@Valid @RequestBody IsUniqueRequest.Nickname request
	) {
		return ResponseEntity.ok(
			ApiResponse.of(userService.isNicknameUnique(request))
		);
	}

	@GetMapping
	public ResponseEntity<ApiResponse<UserResponse>> getCurUser() {
		return ResponseEntity.ok(ApiResponse.of(userService.getUserInfo()));
	}

}
