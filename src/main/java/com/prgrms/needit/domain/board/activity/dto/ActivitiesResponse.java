package com.prgrms.needit.domain.board.activity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ActivitiesResponse {

	@JsonProperty("activities")
	private final List<ActivityResponse> activities;

}
