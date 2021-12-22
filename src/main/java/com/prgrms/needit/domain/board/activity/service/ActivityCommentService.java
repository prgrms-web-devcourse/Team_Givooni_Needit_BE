package com.prgrms.needit.domain.board.activity.service;

import com.prgrms.needit.common.domain.dto.CommentRequest;
import com.prgrms.needit.common.domain.dto.CommentResponse;
import com.prgrms.needit.common.enums.UserType;
import com.prgrms.needit.common.error.ErrorCode;
import com.prgrms.needit.common.error.exception.InvalidArgumentException;
import com.prgrms.needit.common.error.exception.NotFoundResourceException;
import com.prgrms.needit.domain.board.activity.dto.ActivityCommentWriterInfo;
import com.prgrms.needit.domain.board.activity.dto.ActivityCommentsResponse;
import com.prgrms.needit.domain.board.activity.entity.Activity;
import com.prgrms.needit.domain.board.activity.entity.ActivityComment;
import com.prgrms.needit.domain.board.activity.repository.ActivityCommentRepository;
import com.prgrms.needit.domain.board.activity.repository.ActivityRepository;
import com.prgrms.needit.domain.user.user.service.UserService;
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

	public CommentResponse createComment(
		Long activityId,
		CommentRequest request
	) {
		Activity activity = findActivity(activityId);
		ActivityComment createdComment;
		switch (getCurUserType()) {
			case MEMBER:
				createdComment = request.toEntity(
					userService
						.getCurMember()
						.orElseThrow(() ->
										 new InvalidArgumentException(
											 ErrorCode.NOT_FOUND_USER)),
					activity
				);
				break;

			case CENTER:
				createdComment = request.toEntity(
					userService
						.getCurCenter()
						.orElseThrow(() ->
										 new InvalidArgumentException(
											 ErrorCode.NOT_FOUND_USER)),
					activity
				);
				break;

			default:
				throw new InvalidArgumentException(ErrorCode.NOT_FOUND_USER);
		}

		return CommentResponse.toResponse(commentRepository.save(createdComment));
	}

	public CommentResponse modifyComment(
		Long activityId,
		Long commentId,
		CommentRequest request
	) {
		ActivityComment activityComment = findActivityComment(activityId, commentId);
		authorizeCommentAccess(activityComment);
		activityComment.changeComment(request.getComment());
		return CommentResponse.toResponse(activityComment);
	}

	public ActivityCommentsResponse deleteComment(Long activityId, Long commentId) {
		Activity activity = findActivity(activityId);
		ActivityComment activityComment = findActivityComment(activityId, commentId);
		authorizeCommentAccess(activityComment);
		activity.removeComment(activityComment);
		return new ActivityCommentsResponse(
			activity.getComments()
					.stream()
					.map(CommentResponse::toResponse)
					.collect(Collectors.toList()));
	}

	private void authorizeCommentAccess(ActivityComment comment) {
		ActivityCommentWriterInfo writerInfo = comment.getWriterInfo();
		if (!userService.getCurUser()
						.getId()
						.equals(writerInfo.getWriterId()) ||
			!userService.getCurUser()
						.getRole()
						.equals(writerInfo.getWriterType()
										  .name())) {
			throw new InvalidArgumentException(ErrorCode.NOT_MATCH_WRITER);
		}
	}

	private ActivityComment findActivityComment(Long activityId, Long commentId) {
		Activity activity = findActivity(activityId);
		return activity
			.getComments()
			.stream()
			.filter(comment -> comment.getId()
									  .equals(commentId))
			.findFirst()
			.orElseThrow(
				() -> new NotFoundResourceException(
					ErrorCode.NOT_FOUND_ACTIVITY_COMMENT));
	}

	private Activity findActivity(Long id) {
		return activityRepository
			.findById(id)
			.orElseThrow(() -> new NotFoundResourceException(
				ErrorCode.NOT_FOUND_ACTIVITY));
	}

	private UserType getCurUserType() {
		return UserType.valueOf(userService.getCurUser()
										   .getRole());
	}
}
