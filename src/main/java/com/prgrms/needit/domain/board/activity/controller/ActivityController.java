package com.prgrms.needit.domain.board.activity.controller;

import com.prgrms.needit.common.response.ApiResponse;
import com.prgrms.needit.domain.board.activity.controller.bind.ActivityFilterRequest;
import com.prgrms.needit.domain.board.activity.dto.ActivityResponse;
import com.prgrms.needit.domain.board.activity.service.ActivityService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/activities")
public class ActivityController {

	private final ActivityService activityService;

	@GetMapping
	public ResponseEntity<ApiResponse<List<ActivityResponse>>> getRecentActivityPosts(
		Pageable pageable
	) {
		return ResponseEntity.ok(ApiResponse.of(
			activityService.getRecentActivities(pageable)));
	}

	@GetMapping("/search")
	public ResponseEntity<ApiResponse<List<ActivityResponse>>> searchActivityPosts(
		@RequestBody ActivityFilterRequest request,
		Pageable pageable
	) {
		return ResponseEntity.ok(ApiResponse.of(
			activityService.searchActivities(request, pageable)));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<ActivityResponse>> getActivityPost(
		@PathVariable("id") Long id
	) {
		return ResponseEntity.ok(ApiResponse.of(
			activityService.getActivity(id)));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Object>> deleteActivityPost(
		@PathVariable("id") Long id
	) {
		activityService.deleteActivity(id);
		return ResponseEntity.noContent()
							 .build();
	}

}
