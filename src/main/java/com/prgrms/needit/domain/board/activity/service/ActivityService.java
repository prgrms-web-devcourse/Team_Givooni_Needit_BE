package com.prgrms.needit.domain.board.activity.service;

import com.prgrms.needit.common.error.ErrorCode;
import com.prgrms.needit.common.error.exception.InvalidArgumentException;
import com.prgrms.needit.common.error.exception.NotFoundResourceException;
import com.prgrms.needit.domain.board.activity.controller.bind.ActivityFilterRequest;
import com.prgrms.needit.domain.board.activity.dto.ActivityResponse;
import com.prgrms.needit.domain.board.activity.entity.Activity;
import com.prgrms.needit.domain.board.activity.repository.ActivityRepository;
import com.prgrms.needit.domain.user.user.service.UserService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ActivityService {

	private final ActivityRepository activityRepository;
	private final UserService userService;

	@Transactional(readOnly = true)
	public List<ActivityResponse> getRecentActivities(Pageable pageable) {
		if (pageable == null) {
			pageable = PageRequest.of(0, 20);
		}
		return activityRepository.findAll(pageable)
								 .stream()
								 .map(ActivityResponse::new)
								 .collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public List<ActivityResponse> searchActivities(
		ActivityFilterRequest request,
		Pageable pageable
	) {
		return activityRepository.searchAllByTitleOrContentOrActivityType(
			request.getTitle(),
			request.getContent(),
			request.getType(),
			pageable
		)
								 .stream()
								 .map(ActivityResponse::new)
								 .collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public ActivityResponse getActivity(Long id) {
		return activityRepository.findById(id)
								 .map(ActivityResponse::new)
								 .orElseThrow(() -> new NotFoundResourceException(
									 ErrorCode.NOT_FOUND_ACTIVITY));
	}

	public void deleteActivity(Long id) {
		Activity activity = activityRepository
			.findById(id)
			.orElseThrow(() -> new NotFoundResourceException(ErrorCode.NOT_FOUND_ACTIVITY));
		if (activity.getCenter()
					.equals(userService.getCurCenter()
									   .orElseThrow(() -> new NotFoundResourceException(
										   ErrorCode.NOT_FOUND_USER)))) {
			throw new InvalidArgumentException(ErrorCode.NOT_MATCH_WRITER);
		}
		activity.deleteEntity();
	}

}
