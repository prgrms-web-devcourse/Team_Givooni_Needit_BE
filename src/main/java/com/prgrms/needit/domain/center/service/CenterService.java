package com.prgrms.needit.domain.center.service;

import com.prgrms.needit.common.error.ErrorCode;
import com.prgrms.needit.common.error.exception.NotFoundCenterException;
import com.prgrms.needit.domain.center.dto.CenterCreateRequest;
import com.prgrms.needit.domain.center.dto.CenterDetailResponse;
import com.prgrms.needit.domain.center.dto.CenterResponse;
import com.prgrms.needit.domain.center.dto.CenterUpdateRequest;
import com.prgrms.needit.domain.center.entity.Center;
import com.prgrms.needit.domain.center.repository.CenterRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CenterService {

	private final CenterRepository centerRepository;
	private final PasswordEncoder passwordEncoder;

	public CenterService(
		CenterRepository centerRepository,
		PasswordEncoder passwordEncoder
	) {
		this.centerRepository = centerRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Transactional
	public Long createCenter(CenterCreateRequest centerRequest) {
		return centerRepository
			.save(centerRequest.toEntity(passwordEncoder.encode(centerRequest.getPassword())))
			.getId();
	}

	@Transactional(readOnly = true)
	public CenterDetailResponse getCenter(Long centerId) {
		return new CenterDetailResponse(findActiveCenter(centerId));
	}

	@Transactional(readOnly = true)
	public CenterResponse getOtherCenter(Long centerId) {
		return new CenterResponse(findActiveCenter(centerId));
	}

	@Transactional
	public Long updateCenter(Long centerId, CenterUpdateRequest request) {
		Center activeCenter = findActiveCenter(centerId);
		activeCenter.changeCenterInfo(
			request.getEmail(),
			passwordEncoder.encode(request.getPassword()),
			request.getName(),
			request.getContact(),
			request.getAddress(),
			request.getProfileImageUrl(),
			request.getOwner()
		);
		return activeCenter.getId();
	}

	@Transactional
	public void deleteCenter(Long centerId) {
		Center activeCenter = findActiveCenter(centerId);
		activeCenter.deleteEntity();
	}

	@Transactional(readOnly = true)
	public Center findActiveCenter(Long centerId) {
		return centerRepository
			.findByIdAndIsDeletedFalse(centerId)
			.orElseThrow(
				() -> new NotFoundCenterException(ErrorCode.NOT_FOUND_CENTER));
	}
}
