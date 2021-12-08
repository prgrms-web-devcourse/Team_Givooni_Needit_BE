package com.prgrms.needit.domain.member.service;

import com.prgrms.needit.common.email.EmailService;
import com.prgrms.needit.common.error.ErrorCode;
import com.prgrms.needit.common.error.exception.MemberNotFoundException;
import com.prgrms.needit.domain.member.dto.MemberCreateRequest;
import com.prgrms.needit.domain.member.dto.MemberDetailResponse;
import com.prgrms.needit.domain.member.dto.MemberUpdateRequest;
import com.prgrms.needit.domain.member.entity.Member;
import com.prgrms.needit.domain.member.repository.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService implements UserDetailsService {

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

	@Transactional
	public Long createMember(MemberCreateRequest memberRequest) {
		// email 보내고, emailCode 저장
		// 저장된 emailCode와 맞는지 확인
		return memberRepository
			.save(memberRequest.toEntity(passwordEncoder.encode(memberRequest.getPassword())))
			.getId();
	}

	@Transactional(readOnly = true)
	public MemberDetailResponse getMember(Long memberId) {
		return memberRepository
			.findById(memberId)
			.map(MemberDetailResponse::new)
			.orElseThrow(
				() -> new MemberNotFoundException(ErrorCode.NOT_FOUND_MEMBER));
	}

	@Transactional(readOnly = true)
	public Member findActiveMember(Long memberId) {
		return memberRepository
			.findById(memberId)
			.orElseThrow(
				() -> new MemberNotFoundException(ErrorCode.NOT_FOUND_MEMBER));
	}

	// TODO: 2021-12-03 이메일 인증, password 인증
	@Transactional
	public Long updateMember(Long memberId, MemberUpdateRequest request) {
		Member activeMember = findActiveMember(memberId);
		activeMember.changeMemberInfo(request, passwordEncoder.encode(request.getPassword()));
		return activeMember.getId();
	}

	@Transactional
	public void deleteMember(Long memberId) {
		Member activeMember = findActiveMember(memberId);
		activeMember.deleteEntity();
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return null;
	}
}
