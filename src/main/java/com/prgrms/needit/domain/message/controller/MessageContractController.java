package com.prgrms.needit.domain.message.controller;

import com.prgrms.needit.common.response.ApiResponse;
import com.prgrms.needit.domain.message.controller.bind.MessageContractRequest;
import com.prgrms.needit.domain.message.entity.PostType;
import com.prgrms.needit.domain.message.entity.response.MessageContractResponse;
import com.prgrms.needit.domain.message.service.MessageContractService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/message")
public class MessageContractController {

	private final MessageContractService messageContractService;

	@GetMapping("/wish/{wishArticleId}/comments/{commentId}")
	public ResponseEntity<ApiResponse<List<MessageContractResponse>>> readDonationWishCommentMessages(
		@PathVariable("wishArticleId") long wishArticleId,
		@PathVariable("commentId") long commentId,
		Pageable pageable) {
		List<MessageContractResponse> commentMessages = messageContractService.getCommentMessages(
			wishArticleId, commentId, PostType.DONATION_WISH, pageable);
		return ResponseEntity.ok(ApiResponse.of(commentMessages));
	}

	@GetMapping("/donation/{donationArticleId}/comments/{commentId}")
	public ResponseEntity<ApiResponse<List<MessageContractResponse>>> readDonationCommentMessages(
		@PathVariable("donationArticleId") long donationArticleId,
		@PathVariable("commentId") long commentId,
		Pageable pageable) {
		List<MessageContractResponse> commentMessages = messageContractService.getCommentMessages(
			donationArticleId, commentId, PostType.DONATION, pageable);
		return ResponseEntity.ok(ApiResponse.of(commentMessages));
	}

	@GetMapping("/wish/{wishArticleId}/comments/{commentId}/after/{messageId}")
	public ResponseEntity<ApiResponse<List<MessageContractResponse>>> readDonationWishCommentsByCursor(
		@PathVariable("wishArticleId") long wishArticleId,
		@PathVariable("commentId") long commentId,
		@PathVariable("messageId") long messageId) {
		List<MessageContractResponse> messageContractResponses = messageContractService.readMessagesAfter(
			wishArticleId, commentId, PostType.DONATION_WISH, messageId);
		return ResponseEntity.ok(ApiResponse.of(messageContractResponses));
	}

	@GetMapping("/donation/{donationArticleId}/comments/{commentId}/after/{messageId}")
	public ResponseEntity<ApiResponse<List<MessageContractResponse>>> readDonationCommentsByCursor(
		@PathVariable("donationArticleId") long donationArticleId,
		@PathVariable("commentId") long commentId,
		@PathVariable("messageId") long messageId) {
		List<MessageContractResponse> messageContractResponses = messageContractService.readMessagesAfter(
			donationArticleId, commentId, PostType.DONATION, messageId);
		return ResponseEntity.ok(ApiResponse.of(messageContractResponses));
	}

	@PostMapping("/wish/{wishArticleId}/comments/{commentId}")
	public ResponseEntity<ApiResponse<MessageContractResponse>> sendDonationWishRequestToCenter(
		@PathVariable("wishArticleId") long wishArticleId,
		@PathVariable("commentId") long commentId, // <- comment is kind of chat room.
		@AuthenticationPrincipal Object user, // <- JWT Authentication later.
		@RequestBody MessageContractRequest request
	) {
		MessageContractResponse messageContractResponse = messageContractService.sendDonationWishMessage(
			wishArticleId, commentId, 0L, null, request);
		// TODO: parse sender's id and type from authentication principal and pass to params.
		return ResponseEntity.ok(ApiResponse.of(messageContractResponse));
	}

	@PostMapping("/donation/{donationArticleId}/comments/{commentId}")
	public ResponseEntity<ApiResponse<MessageContractResponse>> sendDonationRequestToMember(
		@PathVariable("donationArticleId") long donationArticleId,
		@PathVariable("commentId") long commentId,
		@AuthenticationPrincipal Object user,
		@RequestBody MessageContractRequest request
	) {
		MessageContractResponse messageContractResponse = messageContractService.sendDonationMessage(
			donationArticleId, commentId, 0L, null, request);
		// TODO: parse sender's id and type from authentication principal and pass to params.
		return ResponseEntity.ok(ApiResponse.of(messageContractResponse));
	}

}
