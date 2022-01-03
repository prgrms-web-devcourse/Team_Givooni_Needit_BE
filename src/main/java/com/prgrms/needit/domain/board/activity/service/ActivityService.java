package com.prgrms.needit.domain.board.activity.service;

import com.prgrms.needit.common.domain.service.UploadService;
import com.prgrms.needit.common.error.ErrorCode;
import com.prgrms.needit.common.error.exception.InvalidArgumentException;
import com.prgrms.needit.common.error.exception.NotFoundResourceException;
import com.prgrms.needit.domain.board.activity.controller.bind.ActivityFilterRequest;
import com.prgrms.needit.domain.board.activity.controller.bind.ActivityInformationRequest;
import com.prgrms.needit.domain.board.activity.dto.ActivitiesResponse;
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

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ActivityService {

	private final ActivityRepository activityRepository;
	private final UserService userService;
	private final UploadService uploadService;

	@Transactional(readOnly = true)
	public ActivitiesResponse getRecentActivities(Pageable pageable) {
		if (pageable == null) {
			pageable = PageRequest.of(0, 20);
		}

		return new ActivitiesResponse(
			activityRepository.findAll(pageable)
							  .stream()
							  .map(ActivityResponse::new)
							  .collect(Collectors.toList()));
	}

	@Transactional(readOnly = true)
	public ActivitiesResponse searchActivities(
		ActivityFilterRequest request,
		Pageable pageable
	) {

		return new ActivitiesResponse(
			activityRepository.searchAllByTitleOrContentOrActivityType(
								  request.getTitle(),
								  request.getContent(),
								  request.getType(),
								  pageable
							  )
							  .stream()
							  .map(ActivityResponse::new)
							  .collect(Collectors.toList()));
	}

	@Transactional(readOnly = true)
	public ActivityResponse getActivity(Long id) {

		return activityRepository.findById(id)
								 .map(ActivityResponse::new)
								 .orElseThrow(() -> new NotFoundResourceException(
									 ErrorCode.NOT_FOUND_ACTIVITY));
	}

	private void uploadFilesToActivity(List<MultipartFile> files, Activity activity) {
		files.forEach(file -> {
			if (file.isEmpty()) {
				return;
			}
			try {
				String url = uploadService.upload(file, "activity");
				ActivityImage.registerImage(url, activity);
			} catch (IOException e) {
				log.error("IOException occur when uploading file {} from activity #{}",
						  file.getOriginalFilename(), activity.getId()
				);
			}
		});
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
		uploadFilesToActivity(files, createdActivity);

		return new ActivityResponse(createdActivity);
	}

	public ActivityResponse modifyActivity(
		Long activityId,
		ActivityInformationRequest request,
		List<MultipartFile> files
	) {
		Activity activity = activityRepository
			.findById(activityId)
			.orElseThrow(() -> new NotFoundResourceException(
				ErrorCode.NOT_FOUND_ACTIVITY));
		authorizeActivityAccess(activity);
		activity.clearImages();
		activity.changeInfo(request.getTitle(), request.getContent(), request.getActivityType());
		uploadFilesToActivity(files, activity);

		return new ActivityResponse(activity);
	}

	public void deleteActivity(Long id) {
		Activity activity = activityRepository
			.findById(id)
			.orElseThrow(() -> new NotFoundResourceException(ErrorCode.NOT_FOUND_ACTIVITY));
		authorizeActivityAccess(activity);
		activity.deleteEntity();
	}

	private void authorizeActivityAccess(Activity activity) {
		Center center = userService.getCurCenter()
								   .orElseThrow(() -> new NotFoundResourceException(
									   ErrorCode.NOT_FOUND_USER));
		if (!activity.getCenter()
					 .equals(center)) {
			throw new InvalidArgumentException(ErrorCode.NOT_MATCH_WRITER);
		}
	}

}
