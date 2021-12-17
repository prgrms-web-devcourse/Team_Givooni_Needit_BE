package com.prgrms.needit.domain.board.activity.controller.bind;

import com.prgrms.needit.domain.board.activity.entity.enums.ActivityType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ActivityFilterRequest {

	private String title;
	private String content;
	private ActivityType type;

}
