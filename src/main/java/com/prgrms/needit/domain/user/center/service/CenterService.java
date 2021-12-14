package com.prgrms.needit.domain.user.center.service;

import com.prgrms.needit.common.error.ErrorCode;
import com.prgrms.needit.common.error.exception.NotFoundResourceException;
import com.prgrms.needit.domain.user.center.dto.CenterRequest;
import com.prgrms.needit.domain.user.center.dto.CenterResponse;
import com.prgrms.needit.domain.user.center.dto.CenterSelfResponse;
import com.prgrms.needit.domain.user.center.entity.Center;
import com.prgrms.needit.domain.user.center.repository.CenterRepository;
import com.prgrms.needit.domain.user.login.service.UserService;
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
	public Long createCenter(CenterRequest centerRequest) {
		return centerRepository
			.save(centerRequest.toEntity(passwordEncoder.encode(centerRequest.getPassword())))
			.getId();
	}

	@Transactional(readOnly = true)
	public CenterSelfResponse getMyInfo() {
		Center curCenter = userService.getCurCenter()
									  .orElseThrow();
		return new CenterSelfResponse(curCenter);
	}

	@Transactional(readOnly = true)
	public CenterResponse getOtherCenter(Long id) {
		return new CenterResponse(findActiveCenter(id));
	}

	@Transactional
	public Long updateCenter(CenterRequest request) {
		Center curCenter = userService.getCurCenter()
									  .orElseThrow();

		curCenter.changeCenterInfo(
			request.getEmail(),
			passwordEncoder.encode(request.getPassword()),
			request.getName(),
			request.getContact(),
			request.getAddress(),
			request.getProfileImageUrl(),
			request.getOwner()
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
