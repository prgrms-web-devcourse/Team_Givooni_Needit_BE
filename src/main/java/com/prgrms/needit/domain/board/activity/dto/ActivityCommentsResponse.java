package com.prgrms.needit.domain.board.activity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.prgrms.needit.common.domain.dto.CommentResponse;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ActivityCommentsResponse {

	@JsonProperty("comments")
	private final List<CommentResponse> comments;

}
