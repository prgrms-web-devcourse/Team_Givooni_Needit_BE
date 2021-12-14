package com.prgrms.needit.domain.user.center.service;

import com.prgrms.needit.common.error.ErrorCode;
import com.prgrms.needit.common.error.exception.NotFoundResourceException;
import com.prgrms.needit.domain.user.center.dto.CenterCreateRequest;
import com.prgrms.needit.domain.user.center.dto.CenterResponse;
import com.prgrms.needit.domain.user.center.dto.CenterUpdateRequest;
import com.prgrms.needit.domain.user.center.entity.Center;
import com.prgrms.needit.domain.user.center.repository.CenterRepository;
import com.prgrms.needit.domain.user.login.service.UserService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CenterService {

	private final UserService userService;
	private final CenterRepository centerRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional(readOnly = true)
	public List<CenterResponse> searchCenter(String center) {
		return centerRepository.findAllByIsDeletedFalseAndNameContaining(center)
							   .stream()
							   .map(CenterResponse::new)
							   .collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public CenterResponse getOtherCenter(Long id) {
		return new CenterResponse(findActiveCenter(id));
	}

	@Transactional
	public Long createCenter(CenterCreateRequest request) {
		return centerRepository
			.save(request.toEntity(passwordEncoder.encode(request.getPassword())))
			.getId();
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

	@Transactional(readOnly = true)
	public Center findActiveCenter(Long centerId) {
		return centerRepository
			.findByIdAndIsDeletedFalse(centerId)
			.orElseThrow(
				() -> new NotFoundResourceException(ErrorCode.NOT_FOUND_CENTER));
	}

}
