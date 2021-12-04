package com.prgrms.needit.domain.board.donation.service;

import com.prgrms.needit.common.domain.ThemeTag;
import com.prgrms.needit.domain.board.donation.dto.DonationRegisterRequest;
import com.prgrms.needit.domain.board.donation.entity.Donation;
import com.prgrms.needit.domain.board.donation.repository.DonationRepository;
import com.prgrms.needit.domain.board.donation.repository.ThemeTagRepository;
import com.prgrms.needit.domain.member.entity.Member;
import com.prgrms.needit.domain.member.repository.MemberRepository;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class DonationService {

	private final MemberRepository memberRepository;
	private final DonationRepository donationRepository;
	private final ThemeTagRepository themeTagRepository;

	public DonationService(
		MemberRepository memberRepository,
		DonationRepository donationRepository,
		ThemeTagRepository themeTagRepository
	) {
		this.memberRepository = memberRepository;
		this.donationRepository = donationRepository;
		this.themeTagRepository = themeTagRepository;
	}

	@Transactional
	public Long registerDonation(DonationRegisterRequest request) {
		Member member = memberRepository.findById(1L)
										.get();

		Donation donation = request.toEntity();
		donation.addMember(member);

		for (Long tagId : request.getTags()) {
			ThemeTag themeTag = themeTagRepository.findById(tagId)
												  .get();
			donation.addTag(themeTag);
		}

		return donationRepository
			.save(donation)
			.getId();
	}
}
