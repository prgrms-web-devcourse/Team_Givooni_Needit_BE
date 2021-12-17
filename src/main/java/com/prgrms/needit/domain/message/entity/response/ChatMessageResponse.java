package com.prgrms.needit.domain.message.entity.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.prgrms.needit.common.enums.BoardType;
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

	@JsonProperty("postId")
	private final Long postId;

	@JsonProperty("postType")
	private final BoardType postType;

	@JsonProperty("content")
	private final String content;

	@JsonProperty("member")
	private final ChatUserResponse memberResponse;

	@JsonProperty("center")
	private final ChatUserResponse centerResponse;

	@JsonProperty("senderType")
	private final UserType senderType;

	@JsonProperty("contract")
	private final ContractResponse contract;

	public ChatMessageResponse(ChatMessage message) {
		this.id = message.getId();
		this.content = message.getContent();
		if (message.getDonation() != null && message.getDonationWish() == null) {
			this.postId = message.getDonation()
								 .getId();
			this.postType = BoardType.DONATION;
		} else if (message.getDonationWish() != null && message.getDonation() == null) {
			this.postId = message.getDonationWish()
								 .getId();
			this.postType = BoardType.WISH;
		} else {
			this.postId = -1L;
			this.postType = null;
		}
		this.memberResponse = ChatUserResponse.ofMember(message.getMember());
		this.centerResponse = ChatUserResponse.ofCenter(message.getCenter());
		this.senderType = message.getSenderType();
		this.contract = message.getContract() == null ?
			null :
			new ContractResponse(message.getContract());
	}

}
