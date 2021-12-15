package com.prgrms.needit.domain.message.entity.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.prgrms.needit.common.enums.UserType;
import com.prgrms.needit.domain.contract.entity.response.ContractResponse;
import com.prgrms.needit.domain.message.entity.ChatMessage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ChatMessageResponse {

	@JsonProperty("messageId")
	private final Long id;

	@JsonProperty("content")
	private final String content;

	@JsonProperty("memberId")
	private final Long memberId;

	@JsonProperty("memberName")
	private final String memberName;

	@JsonProperty("centerId")
	private final Long centerId;

	@JsonProperty("centerName")
	private final String centerName;

	@JsonProperty("senderType")
	private final UserType senderType;

	@JsonProperty("contract")
	private final ContractResponse contract;

	public ChatMessageResponse(ChatMessage message) {
		this.id = message.getId();
		this.content = message.getContent();
		this.memberId = message.getMember().getId();
		this.memberName = message.getMember().getNickname();
		this.centerId = message.getCenter().getId();
		this.centerName = message.getCenter().getName();
		this.senderType = message.getSenderType();
		this.contract = message.getContract() == null ?
			null :
			new ContractResponse(message.getContract());
	}

}
