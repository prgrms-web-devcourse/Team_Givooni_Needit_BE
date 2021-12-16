package com.prgrms.needit.domain.user.member.service;

import com.prgrms.needit.common.domain.service.UploadService;
import com.prgrms.needit.common.error.ErrorCode;
import com.prgrms.needit.common.error.exception.NotFoundResourceException;
import com.prgrms.needit.domain.user.user.service.UserService;
import com.prgrms.needit.domain.user.member.dto.MemberCreateRequest;
import com.prgrms.needit.domain.user.member.dto.MemberResponse;
import com.prgrms.needit.domain.user.member.dto.MemberUpdateRequest;
import com.prgrms.needit.domain.user.member.entity.Member;
import com.prgrms.needit.domain.user.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import java.io.IOException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class MemberService {

	private static final String DIRNAME = "member";
	private static final String DEFAULT_FILE_URL = "https://d2u3dcdbebyaiu.cloudfront.net/uploads/atch_img/436/8142f53e51d2ec31bc0fa4bec241a919_crop.jpeg";

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	private final UserService userService;
	private final UploadService uploadService;

	@Transactional
	public Long createMember(MemberCreateRequest request) {
		return memberRepository
			.save(
				request.toEntity(
					passwordEncoder.encode(request.getPassword())
				))
			.getId();
	}

	@Transactional(readOnly = true)
	public MemberResponse getOtherMember(Long id) {
		return new MemberResponse(findActiveMember(id));
	}

	@Transactional
	public Long updateMember(MultipartFile file, MemberUpdateRequest request) throws IOException {
		Member curMember = userService.getCurMember()
									  .orElseThrow();

		String newImage = "";
		if (file != null) {
			newImage = registerImage(file);
		} else {
			newImage = DEFAULT_FILE_URL;
		}

		curMember.changeMemberInfo(
			request.getEmail(),
			passwordEncoder.encode(request.getPassword()),
			request.getNickname(),
			request.getContact(),
			request.getAddress(),
			newImage,
			request.getIntroduction()
		);

		return curMember.getId();
	}

	@Transactional
	public void deleteMember() {
		Member curMember = userService.getCurMember()
									  .orElseThrow();
		curMember.deleteEntity();
	}

	public Member findActiveMember(Long memberId) {
		return memberRepository
			.findByIdAndIsDeletedFalse(memberId)
			.orElseThrow(
				() -> new NotFoundResourceException(ErrorCode.NOT_FOUND_MEMBER));
	}

	private String registerImage(MultipartFile newImage) throws IOException {
		return uploadService.upload(newImage, DIRNAME);
	}
}
