package com.prgrms.needit.domain.board.donation.controller;

import com.prgrms.needit.common.response.ApiResponse;
import com.prgrms.needit.domain.board.donation.dto.DonationRegisterRequest;
import com.prgrms.needit.domain.board.donation.service.DonationService;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/donation")
public class DonationController {

	private final DonationService donationService;

	public DonationController(DonationService donationService) {
		this.donationService = donationService;
	}

	@PostMapping
	public ResponseEntity<ApiResponse<Long>> registerDonation(
		@Valid @RequestBody DonationRegisterRequest request
	) {
		return ResponseEntity.ok(ApiResponse.of(donationService.registerDonation(request)));
	}
}
