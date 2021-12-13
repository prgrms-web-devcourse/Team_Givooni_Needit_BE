package com.prgrms.needit.domain.user.member.service;

import com.prgrms.needit.common.error.ErrorCode;
import com.prgrms.needit.common.error.exception.NotFoundResourceException;
import com.prgrms.needit.domain.user.login.service.UserService;
import com.prgrms.needit.domain.user.member.dto.MemberRequest;
import com.prgrms.needit.domain.user.member.dto.MemberResponse;
import com.prgrms.needit.domain.user.member.dto.MemberSelfResponse;
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
	public Long createMember(MemberRequest memberRequest) {
		return memberRepository
			.save(
				memberRequest.toEntity(
					passwordEncoder.encode(memberRequest.getPassword())
				))
			.getId();
	}

	@Transactional(readOnly = true)
	public MemberSelfResponse getMyInfo() {
		Member curMember = userService.getCurMember()
									  .orElseThrow();
		return new MemberSelfResponse(curMember);
	}

	@Transactional(readOnly = true)
	public MemberResponse getOtherMember(Long id) {
		return new MemberResponse(findActiveMember(id));
	}

	@Transactional
	public Long updateMember(MemberRequest request) {
		Member curMember = userService.getCurMember()
									  .orElseThrow();

		curMember.changeMemberInfo(
			request.getEmail(),
			passwordEncoder.encode(request.getPassword()),
			request.getNickname(),
			request.getContact(),
			request.getAddress(),
			request.getProfileImageUrl()
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
