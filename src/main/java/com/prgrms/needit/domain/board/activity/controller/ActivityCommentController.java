package com.prgrms.needit.domain.board.activity.controller;

import com.prgrms.needit.common.response.ApiResponse;
import com.prgrms.needit.domain.board.activity.dto.ActivityCommentResponse;
import com.prgrms.needit.domain.board.activity.service.ActivityCommentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/activities/{activityId}/comments")
public class ActivityCommentController {

	private final ActivityCommentService commentService;

	@GetMapping
	public ResponseEntity<ApiResponse<List<ActivityCommentResponse>>> getComments(
		@PathVariable("activityId") Long activityId
	) {
		return ResponseEntity.ok(ApiResponse.of(
			commentService.getComments(activityId)));
	}

	@DeleteMapping("/{commentId}")
	public ResponseEntity<ApiResponse<List<ActivityCommentResponse>>> deleteComments(
		@PathVariable("activityId") Long activityId,
		@PathVariable("commentId") Long commentId
	) {
		return ResponseEntity.ok(ApiResponse.of(
			commentService.deleteComment(activityId, commentId)));
	}

}
