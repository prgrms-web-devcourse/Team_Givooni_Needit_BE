package com.prgrms.needit.domain.user.login.controller;

import com.prgrms.needit.common.domain.dto.DonationsResponse;
import com.prgrms.needit.common.response.ApiResponse;
import com.prgrms.needit.domain.board.donation.service.DonationService;
import com.prgrms.needit.domain.board.wish.service.DonationWishService;
import com.prgrms.needit.domain.user.login.dto.CurUser;
import com.prgrms.needit.domain.user.login.dto.IsUniqueRequest;
import com.prgrms.needit.domain.user.login.dto.IsUniqueResponse;
import com.prgrms.needit.domain.user.login.dto.LoginRequest;
import com.prgrms.needit.domain.user.login.dto.TokenResponse;
import com.prgrms.needit.domain.user.login.service.UserService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	private final DonationService donationService;
	private final DonationWishService donationWishService;

	@PostMapping("/login")
	public ResponseEntity<ApiResponse<TokenResponse>> login(
		@Valid @RequestBody LoginRequest login
	) {
		return ResponseEntity.ok(ApiResponse.of(userService.login(login)));
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
	public ResponseEntity<ApiResponse<CurUser>> getCurUser() {
		return ResponseEntity.ok(ApiResponse.of(userService.getCurUser()));
	}

	@GetMapping("/donations")
	public ResponseEntity<ApiResponse<List<DonationsResponse>>> getMyDonations() {
		return ResponseEntity.ok(ApiResponse.of(donationService.getMyDonations()));
	}

	@GetMapping("/wishes")
	public ResponseEntity<ApiResponse<List<DonationsResponse>>> getMyDonationWishes() {
		return ResponseEntity.ok(ApiResponse.of(donationWishService.getMyDonationWishes()));
	}

}
