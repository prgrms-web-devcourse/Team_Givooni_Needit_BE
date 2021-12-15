package com.prgrms.needit.domain.user.center.service;

import com.prgrms.needit.common.error.ErrorCode;
import com.prgrms.needit.common.error.exception.NotFoundResourceException;
import com.prgrms.needit.domain.user.center.dto.CenterCreateRequest;
import com.prgrms.needit.domain.user.center.dto.CenterResponse;
import com.prgrms.needit.domain.user.center.dto.CenterUpdateRequest;
import com.prgrms.needit.domain.user.center.entity.Center;
import com.prgrms.needit.domain.user.center.repository.CenterRepository;
import com.prgrms.needit.domain.user.user.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CenterService {

	private final CenterRepository centerRepository;
	private final PasswordEncoder passwordEncoder;
	private final UserService userService;

	public CenterService(
		CenterRepository centerRepository,
		PasswordEncoder passwordEncoder,
		UserService userService
	) {
		this.centerRepository = centerRepository;
		this.passwordEncoder = passwordEncoder;
		this.userService = userService;
	}

	@Transactional
	public Long createCenter(CenterCreateRequest request) {
		return centerRepository
			.save(request.toEntity(passwordEncoder.encode(request.getPassword())))
			.getId();
	}

	@Transactional(readOnly = true)
	public CenterResponse getOtherCenter(Long id) {
		return new CenterResponse(findActiveCenter(id));
	}

	@Transactional
	public Long updateCenter(CenterUpdateRequest request) {
		Center curCenter = userService.getCurCenter()
									  .orElseThrow();

		curCenter.changeCenterInfo(
			request.getEmail(),
			passwordEncoder.encode(request.getPassword()),
			request.getName(),
			request.getContact(),
			request.getAddress(),
			request.getProfileImageUrl(),
			request.getOwner(),
			request.getIntroduction()
		);

		return curCenter.getId();
	}

	@Transactional
	public void deleteCenter() {
		Center curCenter = userService.getCurCenter()
									  .orElseThrow();
		curCenter.deleteEntity();
	}

	public Center findActiveCenter(Long centerId) {
		return centerRepository
			.findByIdAndIsDeletedFalse(centerId)
			.orElseThrow(
				() -> new NotFoundResourceException(ErrorCode.NOT_FOUND_CENTER));
	}
}
