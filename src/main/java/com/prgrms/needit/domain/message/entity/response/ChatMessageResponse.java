package com.prgrms.needit.domain.message.entity.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.prgrms.needit.common.domain.enums.UserType;
import com.prgrms.needit.domain.contract.entity.response.ContractResponse;
import com.prgrms.needit.domain.message.entity.ChatMessage;
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

	@JsonProperty("senderType")
	private final UserType senderType;

	@JsonProperty("contract")
	private final ContractResponse contract;

	public ChatMessageResponse(ChatMessage message) {
		this.id = message.getId();
		this.member = message.getMember();
		this.center = message.getCenter();
		this.senderType = message.getSenderType();
		this.contract = message.getContract() == null ?
			null :
			new ContractResponse(message.getContract());
	}

}
