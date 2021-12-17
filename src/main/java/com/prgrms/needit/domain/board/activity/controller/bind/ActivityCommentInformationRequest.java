package com.prgrms.needit.domain.board.activity.controller.bind;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ActivityCommentInformationRequest {

	@NotBlank
	String comment;

}
