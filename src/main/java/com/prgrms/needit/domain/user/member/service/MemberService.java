package com.prgrms.needit.domain.user.member.service;

import com.prgrms.needit.common.domain.dto.DonationsResponse;
import com.prgrms.needit.common.domain.service.UploadService;
import com.prgrms.needit.common.error.ErrorCode;
import com.prgrms.needit.common.error.exception.NotFoundResourceException;
import com.prgrms.needit.domain.board.donation.repository.DonationRepository;
import com.prgrms.needit.domain.user.member.dto.MemberCreateRequest;
import com.prgrms.needit.domain.user.member.dto.MemberUpdateRequest;
import com.prgrms.needit.domain.user.member.entity.Member;
import com.prgrms.needit.domain.user.member.repository.MemberRepository;
import com.prgrms.needit.domain.user.user.dto.CurUser;
import com.prgrms.needit.domain.user.user.dto.UserResponse;
import com.prgrms.needit.domain.user.user.service.UserService;
import java.io.IOException;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class MemberService {

	private static final String DIRNAME = "member";
	private static final String DEFAULT_FILE_URL = "https://d2u3dcdbebyaiu.cloudfront.net/uploads/atch_img/436/8142f53e51d2ec31bc0fa4bec241a919_crop.jpeg";

	private final UserService userService;
	private final UploadService uploadService;
	private final PasswordEncoder passwordEncoder;
	private final MemberRepository memberRepository;
	private final DonationRepository donationRepository;

	@Transactional
	public Long createMember(MemberCreateRequest request) {
		return memberRepository
			.save(
				request.toEntity(passwordEncoder.encode(request.getPassword()))
			)
			.getId();
	}

	@Transactional(readOnly = true)
	public UserResponse getOtherMember(Long id) {
		Member member = findActiveMember(id);

		return new UserResponse(
			CurUser.toResponse(member),
			donationRepository.findAllByMemberAndIsDeletedFalse(member)
							  .stream()
							  .map(DonationsResponse::toResponse)
							  .collect(Collectors.toList()),
			null
		);
	}

	@Transactional
	public Long updateMember(MultipartFile file, MemberUpdateRequest request) throws IOException {
		Member curMember = userService.getCurMember()
									  .orElseThrow();

		String newImage = "";
		if (!file.isEmpty()) {
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
