package com.prgrms.needit.domain.user.login.controller;

import com.prgrms.needit.common.domain.dto.IsUniqueRequest;
import com.prgrms.needit.common.domain.dto.IsUniqueResponse;
import com.prgrms.needit.common.response.ApiResponse;
import com.prgrms.needit.domain.user.login.dto.LoginRequest;
import com.prgrms.needit.domain.user.login.dto.TokenResponse;
import com.prgrms.needit.domain.user.login.service.UserService;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/login")
	public ResponseEntity<ApiResponse<TokenResponse>> login(
		@Valid @RequestBody LoginRequest login
	) {
		return ResponseEntity.ok(ApiResponse.of(userService.login(login)));
	}

	@PostMapping("/checkEmail")
	public ResponseEntity<ApiResponse<IsUniqueResponse>> checkEmail(
		@Valid @RequestBody IsUniqueRequest.Email request
	) {
		return ResponseEntity.ok(
			ApiResponse.of(userService.isEmailUnique(request))
		);
	}

	@PostMapping("/checkNickname")
	public ResponseEntity<ApiResponse<IsUniqueResponse>> checkNickname(
		@Valid @RequestBody IsUniqueRequest.Nickname request
	) {
		return ResponseEntity.ok(
			ApiResponse.of(userService.isNicknameUnique(request))
		);
	}
}
