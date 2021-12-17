package com.prgrms.needit.domain.board.activity.controller;

import com.prgrms.needit.common.response.ApiResponse;
import com.prgrms.needit.domain.board.activity.controller.bind.ActivityFilterRequest;
import com.prgrms.needit.domain.board.activity.controller.bind.ActivityInformationRequest;
import com.prgrms.needit.domain.board.activity.dto.ActivityResponse;
import com.prgrms.needit.domain.board.activity.service.ActivityService;
import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

	@PostMapping
	public ResponseEntity<ApiResponse<ActivityResponse>> createActivityPost(
		List<MultipartFile> file,
		@Valid @RequestBody ActivityInformationRequest request
	) {
		ActivityResponse createdActivity = activityService.createActivity(request, file);
		return ResponseEntity
			.created(URI.create("/activity/" + createdActivity.getId()))
			.body(ApiResponse.of(createdActivity));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<ActivityResponse>> modifyActivityPost(
		@PathVariable("id") long activityId,
		List<MultipartFile> file,
		@Valid @RequestBody ActivityInformationRequest request
	) {
		return ResponseEntity.ok(ApiResponse.of(
			activityService.modifyActivity(activityId, request, file)));
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
