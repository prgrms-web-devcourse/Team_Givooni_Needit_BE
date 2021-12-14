package com.prgrms.needit.domain.board.wish.controller;

import com.prgrms.needit.common.domain.dto.CommentRequest;
import com.prgrms.needit.common.domain.dto.DealStatusRequest;
import com.prgrms.needit.common.domain.dto.DonationsResponse;
import com.prgrms.needit.common.domain.dto.PageRequest;
import com.prgrms.needit.common.response.ApiResponse;
import com.prgrms.needit.domain.board.wish.dto.DonationWishFilterRequest;
import com.prgrms.needit.domain.board.wish.dto.DonationWishRequest;
import com.prgrms.needit.domain.board.wish.dto.DonationWishResponse;
import com.prgrms.needit.domain.board.wish.service.DonationWishService;
import com.prgrms.needit.domain.board.wish.service.WishCommentService;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wishes")
public class DonationWishController {

	private final DonationWishService donationWishService;
	private final WishCommentService wishCommentService;

	public DonationWishController(
		DonationWishService donationWishService,
		WishCommentService wishCommentService
	) {
		this.donationWishService = donationWishService;
		this.wishCommentService = wishCommentService;
	}

	@GetMapping("/search")
	public ResponseEntity<ApiResponse<Page<DonationsResponse>>> getDonationWishes(
		@ModelAttribute DonationWishFilterRequest request,
		PageRequest pageRequest
	) {
		return ResponseEntity.ok(
			ApiResponse.of(donationWishService.getDonationWishes(
				request,
				pageRequest.of()
			))
		);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<DonationWishResponse>> getDonationWish(
		@PathVariable Long id
	) {
		return ResponseEntity.ok(
			ApiResponse.of(donationWishService.getDonationWish(id))
		);
	}

	@PostMapping
	public ResponseEntity<ApiResponse<Long>> registerDonationWish(
		@Valid @RequestBody DonationWishRequest request
	) {
		return ResponseEntity.ok(ApiResponse.of(
			donationWishService.registerDonationWish(request))
		);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<Long>> modifyDonationWish(
		@PathVariable Long id,
		@Valid @RequestBody DonationWishRequest request
	) {
		return ResponseEntity.ok(
			ApiResponse.of(donationWishService.modifyDonationWish(id, request))
		);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<ApiResponse<Long>> modifyDealStatus(
		@PathVariable Long id,
		@Valid @RequestBody DealStatusRequest request
	) {
		return ResponseEntity.ok(
			ApiResponse.of(donationWishService.modifyDealStatus(id, request))
		);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> removeDonationWish(@PathVariable Long id) {
		donationWishService.removeDonationWish(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@PostMapping("/{id}/comments")
	public ResponseEntity<ApiResponse<Long>> registerComment(
		@PathVariable Long id,
		@Valid @RequestBody CommentRequest request
	) {
		return ResponseEntity.ok(
			ApiResponse.of(wishCommentService.registerComment(id, request))
		);
	}

	@DeleteMapping("/{wishId}/comments/{commentId}")
	public ResponseEntity<Void> removeComment(
		@PathVariable Long wishId,
		@PathVariable Long commentId
	) {
		wishCommentService.removeComment(wishId, commentId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
