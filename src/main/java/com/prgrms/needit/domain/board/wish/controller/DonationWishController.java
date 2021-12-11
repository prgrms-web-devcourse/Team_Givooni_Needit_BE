package com.prgrms.needit.domain.board.wish.controller;

import com.prgrms.needit.common.response.ApiResponse;
import com.prgrms.needit.domain.board.donation.dto.DonationStatusRequest;
import com.prgrms.needit.domain.board.wish.dto.DonationWishRequest;
import com.prgrms.needit.domain.board.wish.service.DonationWishService;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wishes")
public class DonationWishController {

	private final DonationWishService donationWishService;

	public DonationWishController(DonationWishService donationWishService) {
		this.donationWishService = donationWishService;
	}

	@PostMapping
	public ResponseEntity<ApiResponse<Long>> registerDonation(
		@Valid @RequestBody DonationWishRequest request
	) {
		return ResponseEntity.ok(ApiResponse.of(
			donationWishService.registerDonationWish(request))
		);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<Long>> modifyDonation(
		@PathVariable Long id,
		@Valid @RequestBody DonationWishRequest request
	) {
		return ResponseEntity.ok(
			ApiResponse.of(donationWishService.modifyDonationWish(id, request)));
	}

	@PatchMapping("/{id}")
	public ResponseEntity<ApiResponse<Long>> modifyDealStatus(
		@PathVariable Long id,
		@Valid @RequestBody DonationStatusRequest request
	) {
		return ResponseEntity.ok(ApiResponse.of(donationWishService.modifyDealStatus(id, request)));
	}
}
