package com.prgrms.needit.domain.board.activity.controller;

import com.prgrms.needit.common.domain.dto.CommentRequest;
import com.prgrms.needit.common.domain.dto.CommentResponse;
import com.prgrms.needit.common.response.ApiResponse;
import com.prgrms.needit.domain.board.activity.controller.bind.ActivityFilterRequest;
import com.prgrms.needit.domain.board.activity.controller.bind.ActivityInformationRequest;
import com.prgrms.needit.domain.board.activity.dto.ActivitiesResponse;
import com.prgrms.needit.domain.board.activity.dto.ActivityCommentsResponse;
import com.prgrms.needit.domain.board.activity.dto.ActivityResponse;
import com.prgrms.needit.domain.board.activity.service.ActivityCommentService;
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
	private final ActivityCommentService commentService;

	@GetMapping
	public ResponseEntity<ApiResponse<ActivitiesResponse>> getRecentActivityPosts(
		Pageable pageable
	) {
		return ResponseEntity.ok(ApiResponse.of(
			activityService.getRecentActivities(pageable)));
	}

	@GetMapping("/search")
	public ResponseEntity<ApiResponse<ActivitiesResponse>> searchActivityPosts(
		ActivityFilterRequest request,
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

	@PostMapping("/{activityId}/comments")
	public ResponseEntity<ApiResponse<CommentResponse>> createComment(
		@PathVariable("activityId") Long activityId,
		@Valid @RequestBody CommentRequest request
	) {
		CommentResponse comment = commentService.createComment(activityId, request);
		return ResponseEntity
			.created(URI.create(
				String.format("/activities/%d/comments/%d", activityId, comment.getId())))
			.body(ApiResponse.of(comment));
	}

	@PutMapping("/{activityId}/comments/{commentId}")
	public ResponseEntity<ApiResponse<CommentResponse>> modifyComment(
		@PathVariable("activityId") Long activityId,
		@PathVariable("commentId") Long commentId,
		@Valid @RequestBody CommentRequest request
	) {
		return ResponseEntity.ok(ApiResponse.of(
			commentService.modifyComment(activityId, commentId, request)));
	}

	@DeleteMapping("/{activityId}/comments/{commentId}")
	public ResponseEntity<ApiResponse<ActivityCommentsResponse>> deleteComments(
		@PathVariable("activityId") Long activityId,
		@PathVariable("commentId") Long commentId
	) {
		return ResponseEntity.ok(ApiResponse.of(
			commentService.deleteComment(activityId, commentId)));
	}

}
