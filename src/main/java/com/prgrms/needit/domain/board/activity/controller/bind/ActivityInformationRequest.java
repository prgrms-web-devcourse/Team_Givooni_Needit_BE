package com.prgrms.needit.domain.board.activity.controller.bind;

import com.prgrms.needit.domain.board.activity.entity.enums.ActivityType;
import javax.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ActivityInformationRequest {

	@NotBlank
	private String title;

	@NotBlank
	private String content;

	@NotBlank
	private ActivityType activityType;

}
