package com.prgrms.needit.domain.board.donation.controller;

import com.prgrms.needit.common.domain.dto.CommentRequest;
import com.prgrms.needit.common.response.ApiResponse;
import com.prgrms.needit.domain.board.donation.dto.DonationRequest;
import com.prgrms.needit.domain.board.donation.dto.DonationStatusRequest;
import com.prgrms.needit.domain.board.donation.service.CommentService;
import com.prgrms.needit.domain.board.donation.service.DonationService;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/donation")
public class DonationController {

	private final DonationService donationService;
	private final CommentService commentService;

	public DonationController(
		DonationService donationService,
		CommentService commentService
	) {
		this.donationService = donationService;
		this.commentService = commentService;
	}

	@PostMapping
	public ResponseEntity<ApiResponse<Long>> registerDonation(
		@Valid @RequestBody DonationRequest request
	) {
		return ResponseEntity.ok(ApiResponse.of(donationService.registerDonation(request)));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<Long>> modifyDonation(
		@PathVariable Long id,
		@Valid @RequestBody DonationRequest request
	) {
		return ResponseEntity.ok(ApiResponse.of(donationService.modifyDonation(id, request)));
	}

	@PatchMapping("/{id}")
	public ResponseEntity<ApiResponse<Long>> modifyDealStatus(
		@PathVariable Long id,
		@Valid @RequestBody DonationStatusRequest request
	) {
		return ResponseEntity.ok(ApiResponse.of(donationService.modifyDealStatus(id, request)));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> removeDonation(@PathVariable Long id) {
		donationService.removeDonation(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@PostMapping("/{id}/comments")
	public ResponseEntity<ApiResponse<Long>> registerComment(
		@PathVariable Long id,
		@Valid @RequestBody CommentRequest request
	) {
		return ResponseEntity.ok(ApiResponse.of(commentService.registerComment(id, request)));
	}

}
