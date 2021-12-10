package com.prgrms.needit.domain.member.service;

import com.prgrms.needit.common.error.ErrorCode;
import com.prgrms.needit.common.error.exception.NotFoundResourceException;
import com.prgrms.needit.domain.member.dto.MemberDetailResponse;
import com.prgrms.needit.domain.member.dto.MemberRequest;
import com.prgrms.needit.domain.member.entity.Member;
import com.prgrms.needit.domain.member.repository.MemberRepository;
import com.prgrms.needit.domain.user.email.service.EmailService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	private final EmailService emailService;

	public MemberService(
		MemberRepository memberRepository,
		PasswordEncoder passwordEncoder,
		EmailService emailService
	) {
		this.memberRepository = memberRepository;
		this.passwordEncoder = passwordEncoder;
		this.emailService = emailService;
	}

	@Transactional(readOnly = true)
	public MemberDetailResponse getMember(Long id) {
		return new MemberDetailResponse(findActiveMember(id));
	}

	@Transactional
	public Long signUpMember(MemberRequest memberRequest) {
		return memberRepository
			.save(memberRequest.toEntity(passwordEncoder.encode(memberRequest.getPassword())))
			.getId();
	}

	@Transactional
	public Long updateMember(Long memberId, MemberRequest request) {
		Member activeMember = findActiveMember(memberId);
		activeMember.changeMemberInfo(
			request.getEmail(),
			passwordEncoder.encode(request.getPassword()),
			request.getNickname(),
			request.getContact(),
			request.getAddress(),
			request.getProfileImageUrl()
		);
		return activeMember.getId();
	}

	@Transactional
	public void deleteMember(Long id) {
		Member activeMember = findActiveMember(id);
		activeMember.deleteEntity();
	}

	@Transactional(readOnly = true)
	public Member findActiveMember(Long id) {
		return memberRepository
			.findById(id)
			.orElseThrow(
				() -> new NotFoundResourceException(ErrorCode.NOT_FOUND_MEMBER));
	}

}
