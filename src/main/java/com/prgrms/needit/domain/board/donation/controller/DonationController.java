package com.prgrms.needit.domain.board.donation.controller;

import com.prgrms.needit.common.domain.dto.CommentRequest;
import com.prgrms.needit.common.domain.dto.DealStatusRequest;
import com.prgrms.needit.common.domain.dto.DonationFilterRequest;
import com.prgrms.needit.common.domain.dto.DonationsResponse;
import com.prgrms.needit.common.domain.dto.PageRequest;
import com.prgrms.needit.common.response.ApiResponse;
import com.prgrms.needit.domain.board.donation.dto.DonationRequest;
import com.prgrms.needit.domain.board.donation.dto.DonationResponse;
import com.prgrms.needit.domain.board.donation.service.CommentService;
import com.prgrms.needit.domain.board.donation.service.DonationService;
import java.io.IOException;
import java.util.List;
import javax.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/donations")
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

	@GetMapping("/search")
	public ResponseEntity<ApiResponse<Page<DonationsResponse>>> getDonations(
		@ModelAttribute DonationFilterRequest request,
		PageRequest pageRequest
	) {
		return ResponseEntity.ok(
			ApiResponse.of(donationService.getDonations(
				request,
				pageRequest.of()
			))
		);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<DonationResponse>> getDonation(@PathVariable Long id) {
		return ResponseEntity.ok(ApiResponse.of(donationService.getDonation(id)));
	}

	@PostMapping
	public ResponseEntity<ApiResponse<Long>> registerDonation(
		@RequestPart(required = false) List<MultipartFile> file,
		@Valid @RequestPart DonationRequest request
	) throws IOException {
		return ResponseEntity.ok(
			ApiResponse.of(donationService.registerDonation(file, request))
		);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<Long>> modifyDonation(
		@PathVariable Long id,
		@RequestPart(required = false) List<MultipartFile> file,
		@Valid @RequestPart DonationRequest request
	) throws IOException {
		return ResponseEntity.ok(
			ApiResponse.of(donationService.modifyDonation(id, file, request))
		);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<ApiResponse<Long>> modifyDealStatus(
		@PathVariable Long id,
		@Valid @RequestBody DealStatusRequest request
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

	@DeleteMapping("/{donationId}/comments/{commentId}")
	public ResponseEntity<Void> removeComment(
		@PathVariable Long donationId,
		@PathVariable Long commentId
	) {
		commentService.removeComment(donationId, commentId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
