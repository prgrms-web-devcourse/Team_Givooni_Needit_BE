package com.prgrms.needit.domain.message.controller.bind;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.prgrms.needit.common.enums.BoardType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ChatMessageRequest {

	@NotNull
	@JsonProperty("postId")
	private Long postId;

	@NotNull
	@JsonProperty("postType")
	private BoardType postType;

	@NotNull
	@JsonProperty("receiverId")
	private Long receiverId;

	@JsonProperty("content")
	@NotBlank
	private String content;

}
