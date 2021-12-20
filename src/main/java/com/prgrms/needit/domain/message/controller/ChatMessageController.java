package com.prgrms.needit.domain.message.controller;

import com.prgrms.needit.common.enums.BoardType;
import com.prgrms.needit.common.response.ApiResponse;
import com.prgrms.needit.domain.message.controller.bind.ChatMessageRequest;
import com.prgrms.needit.domain.message.entity.response.ChatMessageResponse;
import com.prgrms.needit.domain.message.service.ChatMessageService;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
	public ResponseEntity<ApiResponse<List<ChatMessageResponse>>> getChats() {
		return ResponseEntity.ok(ApiResponse.of(chatMessageService.getCurrentUserChats()));
	}

	@GetMapping("/posts")
	public ResponseEntity<ApiResponse<List<ChatMessageResponse>>> readChatsBetweenMemberAndCenterOnArticle(
		@NotNull @RequestParam("postId") Long postId,
		@NotNull @RequestParam("postType") BoardType postType,
		@NotNull @RequestParam("receiverId") Long receiverId,
		@RequestParam(value = "messageId", required = false, defaultValue = "0") Long messageId
	) {
		return ResponseEntity.ok(
			ApiResponse.of(
				chatMessageService.getMessagesOnArticle(postId, postType, receiverId, messageId)));
	}

	@PostMapping
	public ResponseEntity<ApiResponse<ChatMessageResponse>> sendChat(
		@Valid @RequestBody ChatMessageRequest request
	) {
		return ResponseEntity.ok(
			ApiResponse.of(chatMessageService.sendMessage(
			request.getPostId(),
			request.getPostType(),
			request.getReceiverId(),
			request.getContent()
		)));
	}

}
