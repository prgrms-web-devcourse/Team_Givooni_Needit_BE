package com.prgrms.needit.domain.message.controller;

import com.prgrms.needit.common.enums.BoardType;
import com.prgrms.needit.common.error.ErrorCode;
import com.prgrms.needit.common.error.exception.CustomException;
import com.prgrms.needit.common.response.ApiResponse;
import com.prgrms.needit.domain.message.controller.bind.ChatMessageRequest;
import com.prgrms.needit.domain.message.entity.response.ChatMessageResponse;
import com.prgrms.needit.domain.message.service.ChatMessageService;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chats")
public class ChatMessageController {

	private final ChatMessageService chatMessageService;

	@GetMapping
	public ResponseEntity<ApiResponse<List<ChatMessageResponse>>> getChats(
		@AuthenticationPrincipal Object user
	) {
		// TODO: condition whether user is center or member.
		return ResponseEntity.ok(
			ApiResponse.of(chatMessageService.getMemberChats(null)));
	}

	@GetMapping("/posts")
	public ResponseEntity<ApiResponse<List<ChatMessageResponse>>> readChatsBetweenMemberAndCenterOnArticle(
		@NotNull @RequestParam("postId") Long postId,
		@NotNull @RequestParam("postType") BoardType postType,
		@NotNull @RequestParam("receiverId") Long receiverId,
		@RequestParam(value = "messageId", required = false, defaultValue = "0") Long messageId,
		@AuthenticationPrincipal Object user
	) {
		List<ChatMessageResponse> chats;
		switch(postType) {
			case DONATION:
				// TODO: get userid from authentication principal
				// and decide whether it should be passed as member or center.
				chats = chatMessageService.getDonationMessages(
					postId, null, null, messageId);
				break;

			case WISH:
				chats = chatMessageService.getWishCommentMessages(
					postId, null, null, messageId);
				break;

			default:
				throw new CustomException(ErrorCode.INVALID_BOARD_TYPE);
		}
		return ResponseEntity.ok(ApiResponse.of(chats));
	}

	@PostMapping
	public ResponseEntity<ApiResponse<ChatMessageResponse>> sendChat(
		@AuthenticationPrincipal Object user, // <- JWT Authentication later.
		@Valid @RequestBody ChatMessageRequest request
	) {
		ChatMessageResponse response;
		switch(request.getPostType()) {
			case DONATION:
				// TODO: finish after spring security.
				response = chatMessageService.sendDonationMessage(
					request.getPostId(), null, null, null, request.getContent());
				break;

			case WISH:
				response = chatMessageService.sendDonationWishMessage(
					request.getPostId(), null, null, null, request.getContent());
				break;

			default:
				throw new CustomException(ErrorCode.INVALID_BOARD_TYPE);
		}
		return ResponseEntity.ok(ApiResponse.of(response));
	}

}
