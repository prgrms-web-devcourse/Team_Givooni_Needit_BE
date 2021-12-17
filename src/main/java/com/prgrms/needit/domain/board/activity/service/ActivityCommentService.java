package com.prgrms.needit.domain.board.activity.service;

import com.prgrms.needit.common.error.ErrorCode;
import com.prgrms.needit.common.error.exception.NotFoundResourceException;
import com.prgrms.needit.domain.board.activity.dto.ActivityCommentResponse;
import com.prgrms.needit.domain.board.activity.entity.Activity;
import com.prgrms.needit.domain.board.activity.entity.ActivityComment;
import com.prgrms.needit.domain.board.activity.repository.ActivityCommentRepository;
import com.prgrms.needit.domain.board.activity.repository.ActivityRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ActivityCommentService {

	private final ActivityRepository activityRepository;
	private final ActivityCommentRepository commentRepository;

	private Activity findActivity(Long id) {
		return activityRepository
			.findById(id)
			.orElseThrow(() -> new NotFoundResourceException(
				ErrorCode.NOT_FOUND_ACTIVITY));
	}

	@Transactional(readOnly = true)
	public List<ActivityCommentResponse> getComments(Long activityId) {
		Activity activity = findActivity(activityId);
		return activity.getComments()
					   .stream()
					   .map(ActivityCommentResponse::new)
					   .collect(Collectors.toList());
	}

	public List<ActivityCommentResponse> deleteComment(Long activityId, Long commentId) {
		Activity activity = findActivity(activityId);
		ActivityComment activityComment = activity
			.getComments()
			.stream()
			.filter(comment -> comment.getId()
									  .equals(commentId))
			.findFirst()
			.orElseThrow(
				() -> new NotFoundResourceException(
					ErrorCode.NOT_FOUND_ACTIVITY_COMMENT));
		activity.removeComment(activityComment);
		return activity.getComments().stream()
			.map(ActivityCommentResponse::new)
			.collect(Collectors.toList());
	}

}
