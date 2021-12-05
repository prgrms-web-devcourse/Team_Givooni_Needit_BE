package com.prgrms.needit.domain.message.controller.bind;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageRequest {

	// 채팅 텍스트.
	@JsonProperty("content")
	@NotBlank
	private String content;

	// 채팅에 포함할 계약 식별자. 즉 계약은 사전에 생성 후 채팅 생성 요청에 포함하여 전송.
	@JsonProperty("contractId")
	private Long contractId;

}
