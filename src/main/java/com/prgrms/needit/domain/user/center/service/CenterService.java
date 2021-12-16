package com.prgrms.needit.domain.user.center.service;

import com.prgrms.needit.common.domain.service.UploadService;
import com.prgrms.needit.common.error.ErrorCode;
import com.prgrms.needit.common.error.exception.NotFoundResourceException;
import com.prgrms.needit.domain.user.center.dto.CenterCreateRequest;
import com.prgrms.needit.domain.user.center.dto.CenterResponse;
import com.prgrms.needit.domain.user.center.dto.CenterUpdateRequest;
import com.prgrms.needit.domain.user.center.entity.Center;
import com.prgrms.needit.domain.user.center.repository.CenterRepository;
import com.prgrms.needit.domain.user.user.service.UserService;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class CenterService {

	private static final String DIRNAME = "center";
	private static final String DEFAULT_FILE_URL = "https://d2u3dcdbebyaiu.cloudfront.net/uploads/atch_img/436/8142f53e51d2ec31bc0fa4bec241a919_crop.jpeg";

	private final UserService userService;
	private final UploadService uploadService;
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
	public Long updateCenter(MultipartFile file, CenterUpdateRequest request) throws IOException {
		Center curCenter = userService.getCurCenter()
									  .orElseThrow();

		String newImage = "";
		if (file != null) {
			newImage = registerImage(file);
		} else {
			newImage = DEFAULT_FILE_URL;
		}

		curCenter.changeCenterInfo(
			request.getEmail(),
			passwordEncoder.encode(request.getPassword()),
			request.getName(),
			request.getContact(),
			request.getAddress(),
			newImage,
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

	private String registerImage(MultipartFile newImage) throws IOException {
		return uploadService.upload(newImage, DIRNAME);
	}
}
