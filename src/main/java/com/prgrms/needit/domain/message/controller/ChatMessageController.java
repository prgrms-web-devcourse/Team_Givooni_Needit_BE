package com.prgrms.needit.domain.message.controller;

import com.prgrms.needit.common.response.ApiResponse;
import com.prgrms.needit.domain.message.controller.bind.ChatMessageRequest;
import com.prgrms.needit.domain.message.entity.response.ChatMessageResponse;
import com.prgrms.needit.domain.message.service.ChatMessageService;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/chat")
public class ChatMessageController {

	private final ChatMessageService chatMessageService;

	@GetMapping("/wish/{wishArticleId}/comments/{commentId}")
	public ResponseEntity<ApiResponse<List<ChatMessageResponse>>> readDonationWishCommentMessages(
		@PathVariable("wishArticleId") long wishArticleId,
		@PathVariable("commentId") long commentId
	) {
		List<ChatMessageResponse> chats = chatMessageService
			.getWishCommentMessages(wishArticleId, commentId, 0L);
		return ResponseEntity.ok(ApiResponse.of(chats));
	}

	@GetMapping("/donation/{donationArticleId}/comments/{commentId}")
	public ResponseEntity<ApiResponse<List<ChatMessageResponse>>> readDonationCommentMessages(
		@PathVariable("donationArticleId") long donationArticleId,
		@PathVariable("commentId") long commentId
	) {
		List<ChatMessageResponse> commentMessages = chatMessageService
			.getDonationCommentMessages(donationArticleId, commentId, 0L);
		return ResponseEntity.ok(ApiResponse.of(commentMessages));
	}

	@GetMapping("/wish/{wishArticleId}/comments/{commentId}/after/{messageId}")
	public ResponseEntity<ApiResponse<List<ChatMessageResponse>>> readDonationWishCommentsByCursor(
		@PathVariable("wishArticleId") long wishArticleId,
		@PathVariable("commentId") long commentId,
		@PathVariable("messageId") long messageId
	) {
		List<ChatMessageResponse> chatMessageResponses = chatMessageService
			.getWishCommentMessages(wishArticleId, commentId, messageId);
		return ResponseEntity.ok(ApiResponse.of(chatMessageResponses));
	}

	@GetMapping("/donation/{donationArticleId}/comments/{commentId}/after/{messageId}")
	public ResponseEntity<ApiResponse<List<ChatMessageResponse>>> readDonationCommentsByCursor(
		@PathVariable("donationArticleId") long donationArticleId,
		@PathVariable("commentId") long commentId,
		@PathVariable("messageId") long messageId
	) {
		List<ChatMessageResponse> chatMessageResponses = chatMessageService
			.getDonationCommentMessages(donationArticleId, commentId, messageId);
		return ResponseEntity.ok(ApiResponse.of(chatMessageResponses));
	}

	@PostMapping("/wish/{wishArticleId}/comments/{commentId}")
	public ResponseEntity<ApiResponse<ChatMessageResponse>> sendDonationWishRequestToCenter(
		@PathVariable("wishArticleId") long wishArticleId,
		@PathVariable("commentId") long commentId, // <- comment is kind of chat room.
		@AuthenticationPrincipal Object user, // <- JWT Authentication later.
		@RequestBody ChatMessageRequest request
	) {
		ChatMessageResponse chatMessageResponse = chatMessageService
			.sendDonationWishMessage(
			wishArticleId, commentId, null, request);
		// TODO: parse sender's id and type from authentication principal and pass to params.
		return ResponseEntity.ok(ApiResponse.of(chatMessageResponse));
	}

	@PostMapping("/donation/{donationArticleId}/comments/{commentId}")
	public ResponseEntity<ApiResponse<ChatMessageResponse>> sendDonationRequestToMember(
		@PathVariable("donationArticleId") long donationArticleId,
		@PathVariable("commentId") long commentId,
		@AuthenticationPrincipal Object user,
		@RequestBody ChatMessageRequest request
	) {
		ChatMessageResponse chatMessageResponse = chatMessageService.sendDonationMessage(
			donationArticleId, commentId, null, request);
		// TODO: parse sender's id and type from authentication principal and pass to params.
		return ResponseEntity.ok(ApiResponse.of(chatMessageResponse));
	}

}
