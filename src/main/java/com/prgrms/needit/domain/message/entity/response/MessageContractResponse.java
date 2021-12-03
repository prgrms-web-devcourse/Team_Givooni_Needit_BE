package com.prgrms.needit.domain.message.entity.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.prgrms.needit.common.domain.enums.UserType;
import com.prgrms.needit.domain.message.entity.ContractStatus;
import com.prgrms.needit.domain.message.entity.MessageContract;
import com.prgrms.needit.domain.message.entity.MessageType;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MessageContractResponse {

	@JsonProperty("messageId")
	private final long id;

	@JsonProperty("senderId")
	private final long senderId;

	@JsonProperty("senderType")
	private final UserType senderType;

	@JsonProperty("receiverId")
	private final long receiverId;

	@JsonProperty("receiverType")
	private final UserType receiverType;

	@JsonProperty("contractDate")
	private final LocalDateTime date;

	@JsonProperty("contractStatus")
	private final ContractStatus contractStatus;

	@JsonProperty("messageType")
	private final MessageType messageType;

	public MessageContractResponse(MessageContract message) {
		this.id = message.getId();
		this.senderId = message.getSenderId();
		this.senderType = message.getSenderType();
		this.receiverId = message.getReceiverId();
		this.receiverType = message.getReceiverType();
		this.date = message.getDate();
		this.contractStatus = message.getStatus();
		this.messageType = message.getMessageType();
	}

}
