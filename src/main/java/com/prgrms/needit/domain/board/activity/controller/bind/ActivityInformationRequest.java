package com.prgrms.needit.domain.board.activity.controller.bind;

import com.prgrms.needit.domain.board.activity.entity.enums.ActivityType;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ActivityInformationRequest {

	@NotBlank
	private String title;

	@NotBlank
	private String content;

	@NotBlank
	private ActivityType activityType;

}
