package com.prgrms.needit.domain.user.member.service;

import com.prgrms.needit.common.error.ErrorCode;
import com.prgrms.needit.common.error.exception.NotFoundResourceException;
import com.prgrms.needit.domain.user.login.service.UserService;
import com.prgrms.needit.domain.user.member.dto.MemberCreateRequest;
import com.prgrms.needit.domain.user.member.dto.MemberResponse;
import com.prgrms.needit.domain.user.member.dto.MemberUpdateRequest;
import com.prgrms.needit.domain.user.member.entity.Member;
import com.prgrms.needit.domain.user.member.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	private final UserService userService;

	public MemberService(
		MemberRepository memberRepository,
		PasswordEncoder passwordEncoder,
		UserService userService
	) {
		this.memberRepository = memberRepository;
		this.passwordEncoder = passwordEncoder;
		this.userService = userService;
	}

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
	public Long updateMember(MemberUpdateRequest request) {
		Member curMember = userService.getCurMember()
									  .orElseThrow();

		curMember.changeMemberInfo(
			request.getEmail(),
			passwordEncoder.encode(request.getPassword()),
			request.getNickname(),
			request.getContact(),
			request.getAddress(),
			request.getProfileImageUrl(),
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
}
