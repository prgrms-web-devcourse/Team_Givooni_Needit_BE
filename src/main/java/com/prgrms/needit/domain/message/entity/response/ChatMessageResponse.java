package com.prgrms.needit.domain.message.entity.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.prgrms.needit.common.enums.UserType;
import com.prgrms.needit.domain.center.dto.CenterResponse;
import com.prgrms.needit.domain.contract.entity.response.ContractResponse;
import com.prgrms.needit.domain.member.dto.MemberResponse;
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

	@JsonProperty("member")
	private final MemberResponse member;

	@JsonProperty("center")
	private final CenterResponse center;

	@JsonProperty("senderType")
	private final UserType senderType;

	@JsonProperty("contract")
	private final ContractResponse contract;

	public ChatMessageResponse(ChatMessage message) {
		this.id = message.getId();
		this.content = message.getContent();
		this.member = new MemberResponse(message.getMember());
		this.center = new CenterResponse(message.getCenter());
		this.senderType = message.getSenderType();
		this.contract = message.getContract() == null ?
			null :
			new ContractResponse(message.getContract());
	}

}
