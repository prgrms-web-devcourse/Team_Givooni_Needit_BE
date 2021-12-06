package com.prgrms.needit.domain.member.service;

import com.prgrms.needit.common.error.ErrorCode;
import com.prgrms.needit.common.error.exception.MemberNotFoundException;
import com.prgrms.needit.domain.member.dto.MemberCreateRequest;
import com.prgrms.needit.domain.member.dto.MemberDetailResponse;
import com.prgrms.needit.domain.member.dto.MemberUpdateRequest;
import com.prgrms.needit.domain.member.entity.Member;
import com.prgrms.needit.domain.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

	private MemberRepository memberRepository;

	public MemberService(MemberRepository memberRepository) {
		this.memberRepository = memberRepository;
	}

	@Transactional
	public Long createMember(MemberCreateRequest memberRequest) {
		return memberRepository
			.save(memberRequest.toEntity())
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
		activeMember.changeMemberInfo(request);
		return activeMember.getId();
	}

	@Transactional
	public void deleteMember(Long memberId) {
		Member activeMember = findActiveMember(memberId);
		activeMember.deleteEntity();
	}

}
