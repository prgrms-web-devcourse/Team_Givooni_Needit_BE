package com.prgrms.needit.domain.user.member.service;

import com.prgrms.needit.common.error.ErrorCode;
import com.prgrms.needit.common.error.exception.NotFoundResourceException;
import com.prgrms.needit.domain.user.center.dto.CenterListResponse;
import com.prgrms.needit.domain.user.center.entity.Center;
import com.prgrms.needit.domain.user.center.repository.CenterRepository;
import com.prgrms.needit.domain.user.login.service.UserService;
import com.prgrms.needit.domain.user.member.dto.MemberCreateRequest;
import com.prgrms.needit.domain.user.member.dto.MemberResponse;
import com.prgrms.needit.domain.user.member.dto.MemberUpdateRequest;
import com.prgrms.needit.domain.user.member.entity.Member;
import com.prgrms.needit.domain.user.member.repository.FavoriteCenterRepository;
import com.prgrms.needit.domain.user.member.repository.MemberRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

	private final MemberRepository memberRepository;
	private final CenterRepository centerRepository;
	private final FavoriteCenterRepository favoriteCenterRepository;
	private final PasswordEncoder passwordEncoder;
	private final UserService userService;

	public MemberService(
		MemberRepository memberRepository,
		CenterRepository centerRepository,
		FavoriteCenterRepository favoriteCenterRepository,
		PasswordEncoder passwordEncoder,
		UserService userService
	) {
		this.memberRepository = memberRepository;
		this.centerRepository = centerRepository;
		this.favoriteCenterRepository = favoriteCenterRepository;
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

	@Transactional
	public List<CenterListResponse> getFavCenters() {
		Member curMember = userService.getCurMember()
									  .orElseThrow();
		return curMember.getFavoriteCenters()
						.stream()
						.map(favCenter -> new CenterListResponse(favCenter.getCenter()))
						.collect(Collectors.toList());
	}

	@Transactional
	public void removeFavoriteCenter(Long centerId) {

		Member member = userService.getCurMember()
								   .orElseThrow();
		Center center = findActiveCenter(centerId);

		member.deleteFavCenter(center);

		favoriteCenterRepository.deleteByCenter(centerId);
	}

	public Member findActiveMember(Long memberId) {
		return memberRepository
			.findByIdAndIsDeletedFalse(memberId)
			.orElseThrow(
				() -> new NotFoundResourceException(ErrorCode.NOT_FOUND_MEMBER));
	}

	public Center findActiveCenter(Long centerId) {
		return centerRepository
			.findByIdAndIsDeletedFalse(centerId)
			.orElseThrow(
				() -> new NotFoundResourceException(ErrorCode.NOT_FOUND_CENTER));
	}

}
