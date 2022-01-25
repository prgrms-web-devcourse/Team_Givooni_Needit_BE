package com.prgrms.needit.domain.user.user.controller;

import com.prgrms.needit.common.response.ApiResponse;
import com.prgrms.needit.domain.user.user.dto.CurUser;
import com.prgrms.needit.domain.user.user.dto.Request;
import com.prgrms.needit.domain.user.user.dto.Response;
import com.prgrms.needit.domain.user.user.service.UserService;
import java.io.IOException;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@GetMapping()
	public ResponseEntity<ApiResponse<Response.UserInfo>> getCurUser() {
		return ResponseEntity.ok(
			ApiResponse.of(userService.getUserInfo())
		);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<Response.UserInfo>> getOtherUser(
		@PathVariable Long id
	) {
		return ResponseEntity.ok(
			ApiResponse.of(userService.getOtherUser(id))
		);
	}

	@GetMapping("/search")
	public ResponseEntity<ApiResponse<List<CurUser>>> searchCenter(
		@RequestParam @NotBlank String centerName
	) {
		return ResponseEntity.ok(
			ApiResponse.of(userService.searchCenter(centerName))
		);
	}

	@PostMapping("/signup")
	public ResponseEntity<ApiResponse<Long>> signUp(
		@Valid @RequestBody Request.Profile signUp
	) {
		return ResponseEntity.ok(ApiResponse.of(userService.signUp(signUp)));
	}

	@PutMapping
	public ResponseEntity<ApiResponse<Long>> modifyProfile(
		@RequestPart(required = false) MultipartFile file,
		@RequestPart @Valid Request.Profile request
	) throws IOException {
		return ResponseEntity.ok(
			ApiResponse.of(userService.modifyProfile(file, request)));
	}

	@DeleteMapping
	public ResponseEntity<Void> removeUser() {
		userService.removeUser();
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

}
