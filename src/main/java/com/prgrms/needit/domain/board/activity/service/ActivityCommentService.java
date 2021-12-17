package com.prgrms.needit.domain.board.activity.service;

import com.prgrms.needit.common.enums.UserType;
import com.prgrms.needit.common.error.ErrorCode;
import com.prgrms.needit.common.error.exception.InvalidArgumentException;
import com.prgrms.needit.common.error.exception.NotFoundResourceException;
import com.prgrms.needit.domain.board.activity.controller.bind.ActivityCommentInformationRequest;
import com.prgrms.needit.domain.board.activity.dto.ActivityCommentResponse;
import com.prgrms.needit.domain.board.activity.entity.Activity;
import com.prgrms.needit.domain.board.activity.entity.ActivityComment;
import com.prgrms.needit.domain.board.activity.repository.ActivityCommentRepository;
import com.prgrms.needit.domain.board.activity.repository.ActivityRepository;
import com.prgrms.needit.domain.user.user.service.UserService;
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
	private final UserService userService;

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

	private UserType getCurUserType() {
		return UserType.valueOf(userService.getCurUser()
										   .getRole());
	}

	public ActivityCommentResponse createComment(
		Long activityId,
		ActivityCommentInformationRequest request
	) {
		Activity activity = findActivity(activityId);
		ActivityComment.ActivityCommentBuilder builder = ActivityComment
			.builder()
			.comment(request.getComment())
			.activity(activity);
		switch (getCurUserType()) {
			case MEMBER:
				builder.member(userService
								   .getCurMember()
								   .orElseThrow(() ->
													new InvalidArgumentException(
														ErrorCode.NOT_FOUND_USER)));
				break;

			case CENTER:
				builder.center(userService
								   .getCurCenter()
								   .orElseThrow(() ->
													new InvalidArgumentException(
														ErrorCode.NOT_FOUND_USER)));
				break;

			default:
				throw new InvalidArgumentException(ErrorCode.NOT_FOUND_USER);
		}

		return new ActivityCommentResponse(
			commentRepository.save(builder.build()));
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
		return activity.getComments()
					   .stream()
					   .map(ActivityCommentResponse::new)
					   .collect(Collectors.toList());
	}

}
