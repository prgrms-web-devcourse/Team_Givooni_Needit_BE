package com.prgrms.needit.domain.board.activity.service;

import com.prgrms.needit.common.domain.service.UploadService;
import com.prgrms.needit.common.error.ErrorCode;
import com.prgrms.needit.common.error.exception.InvalidArgumentException;
import com.prgrms.needit.common.error.exception.NotFoundResourceException;
import com.prgrms.needit.domain.board.activity.controller.bind.ActivityFilterRequest;
import com.prgrms.needit.domain.board.activity.controller.bind.ActivityInformationRequest;
import com.prgrms.needit.domain.board.activity.dto.ActivityResponse;
import com.prgrms.needit.domain.board.activity.entity.Activity;
import com.prgrms.needit.domain.board.activity.entity.ActivityImage;
import com.prgrms.needit.domain.board.activity.repository.ActivityRepository;
import com.prgrms.needit.domain.user.center.entity.Center;
import com.prgrms.needit.domain.user.user.service.UserService;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ActivityService {

	private final ActivityRepository activityRepository;
	private final UserService userService;
	private final UploadService uploadService;

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

	public ActivityResponse createActivity(
		ActivityInformationRequest request,
		List<MultipartFile> files
	) {
		Center center = userService
			.getCurCenter()
			.orElseThrow(
				() -> new InvalidArgumentException(ErrorCode.NOT_FOUND_USER));

		Activity createdActivity = activityRepository.save(
			Activity
				.builder()
				.center(center)
				.title(request.getTitle())
				.content(request.getContent())
				.activityType(
					request.getActivityType())
				.build());

		for (MultipartFile file : files) {
			if (file.isEmpty()) {
				continue;
			}
			try {
				String url = uploadService.upload(file, "activity");
				ActivityImage.registerImage(url, createdActivity);
			} catch (IOException e) {
				log.error("IOException occur when uploading file {} from activity #{}",
						  file.getOriginalFilename(), createdActivity.getId()
				);
			}
		}

		return new ActivityResponse(createdActivity);
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
