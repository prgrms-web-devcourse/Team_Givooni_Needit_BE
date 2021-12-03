package com.prgrms.needit.domain.message.controller.bind;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.prgrms.needit.domain.message.entity.MessageType;
import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageContractRequest {

	@JsonProperty("content")
	@NotBlank
	private String content;

	@JsonProperty("messageType")
	@NotNull
	private MessageType messageType = MessageType.CHAT;

	@JsonProperty("contractDate")
	@NotNull
	private LocalDateTime contractDate = LocalDateTime.now();

}
