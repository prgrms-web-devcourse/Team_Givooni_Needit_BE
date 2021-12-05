package com.prgrms.needit.domain.message.entity.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.prgrms.needit.domain.message.entity.ChatMessage;
import com.prgrms.needit.domain.message.entity.enums.ChatMessageDirection;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ChatMessageResponse {

	@JsonProperty("messageId")
	private final long id;

	// TODO: member response object here.
	@JsonProperty("member")
	private final Object member;

	// TODO: center response object here.
	@JsonProperty("center")
	private final Object center;

	@JsonProperty("direction")
	private final ChatMessageDirection chatMessageDirection;

	// TODO: contract dto.
	@JsonProperty("contract")
	private final Object contract;

	public ChatMessageResponse(ChatMessage message) {
		this.id = message.getId();
		this.member = message.getMember();
		this.center = message.getCenter();
		this.chatMessageDirection = message.getChatMessageDirection();
		this.contract = message.getContract();
	}

}
