package com.prgrms.needit.domain.board.activity.controller.bind;

import com.prgrms.needit.domain.board.activity.entity.enums.ActivityType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ActivityFilterRequest {

	private String title;
	private String content;
	private ActivityType type;

}
