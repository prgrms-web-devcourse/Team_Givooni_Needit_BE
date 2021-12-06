package com.prgrms.needit.domain.message.controller.bind;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ChatMessageRequest {

	@JsonProperty("content")
	@NotBlank
	private String content;

	@JsonProperty("contractId")
	private Long contractId;

}
