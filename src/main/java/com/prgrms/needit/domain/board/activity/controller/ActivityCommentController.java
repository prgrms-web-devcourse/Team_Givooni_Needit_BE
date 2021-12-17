package com.prgrms.needit.domain.board.activity.controller;

import com.prgrms.needit.common.response.ApiResponse;
import com.prgrms.needit.domain.board.activity.controller.bind.ActivityCommentInformationRequest;
import com.prgrms.needit.domain.board.activity.dto.ActivityCommentResponse;
import com.prgrms.needit.domain.board.activity.dto.ActivityCommentsResponse;
import com.prgrms.needit.domain.board.activity.service.ActivityCommentService;
import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/activities/{activityId}/comments")
public class ActivityCommentController {

	private final ActivityCommentService commentService;

	@GetMapping
	public ResponseEntity<ApiResponse<ActivityCommentsResponse>> getComments(
		@PathVariable("activityId") Long activityId
	) {
		return ResponseEntity.ok(ApiResponse.of(
			commentService.getComments(activityId)));
	}

	@GetMapping("/{commentId}")
	public ResponseEntity<ApiResponse<ActivityCommentResponse>> getComment(
		@PathVariable("activityId") Long activityId,
		@PathVariable("commentId") Long commentId
	) {
		return ResponseEntity.ok(ApiResponse.of(
			commentService.getComment(activityId, commentId)));
	}

	@PostMapping
	public ResponseEntity<ApiResponse<ActivityCommentResponse>> createComment(
		@PathVariable("activityId") Long activityId,
		@Valid @RequestBody ActivityCommentInformationRequest request
	) {
		ActivityCommentResponse comment = commentService.createComment(activityId, request);
		return ResponseEntity
			.created(URI.create(
				String.format("/activities/%d/comments/%d", activityId, comment.getId())))
			.body(ApiResponse.of(comment));
	}

	@PutMapping("/{commentId}")
	public ResponseEntity<ApiResponse<ActivityCommentResponse>> modifyComment(
		@PathVariable("activityId") Long activityId,
		@PathVariable("commentId") Long commentId,
		@Valid @RequestBody ActivityCommentInformationRequest request
	) {
		return ResponseEntity.ok(ApiResponse.of(
			commentService.modifyComment(activityId, commentId, request)));
	}

	@DeleteMapping("/{commentId}")
	public ResponseEntity<ApiResponse<ActivityCommentsResponse>> deleteComments(
		@PathVariable("activityId") Long activityId,
		@PathVariable("commentId") Long commentId
	) {
		return ResponseEntity.ok(ApiResponse.of(
			commentService.deleteComment(activityId, commentId)));
	}

}
