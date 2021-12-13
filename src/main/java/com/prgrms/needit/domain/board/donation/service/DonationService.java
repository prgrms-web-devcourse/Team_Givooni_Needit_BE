package com.prgrms.needit.domain.board.donation.service;

import com.prgrms.needit.common.domain.dto.DealStatusRequest;
import com.prgrms.needit.common.domain.entity.ThemeTag;
import com.prgrms.needit.common.domain.repository.ThemeTagRepository;
import com.prgrms.needit.common.domain.service.UploadService;
import com.prgrms.needit.common.enums.DonationStatus;
import com.prgrms.needit.common.error.ErrorCode;
import com.prgrms.needit.common.error.exception.NotFoundResourceException;
import com.prgrms.needit.common.error.exception.NotMatchResourceException;
import com.prgrms.needit.domain.board.donation.dto.DonationFilterRequest;
import com.prgrms.needit.domain.board.donation.dto.DonationRequest;
import com.prgrms.needit.domain.board.donation.dto.DonationResponse;
import com.prgrms.needit.domain.board.donation.entity.Donation;
import com.prgrms.needit.domain.board.donation.entity.DonationImage;
import com.prgrms.needit.domain.board.donation.repository.DonationRepository;
import com.prgrms.needit.domain.board.donation.repository.DonationTagRepository;
import com.prgrms.needit.domain.user.login.service.UserService;
import com.prgrms.needit.domain.user.member.entity.Member;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class DonationService {

	private static final String DIRNAME = "donation";

	private final UserService userService;
	private final UploadService uploadService;
	private final DonationRepository donationRepository;
	private final ThemeTagRepository themeTagRepository;
	private final DonationTagRepository donationTagRepository;

	@Transactional(readOnly = true)
	public Page<DonationResponse> getDonations(
		DonationFilterRequest request, Pageable pageable
	) {
		return donationRepository.searchAllByFilter(request, pageable)
								 .map(DonationResponse::new);
	}

	@Transactional(readOnly = true)
	public DonationResponse getDonation(Long id) {
		return new DonationResponse(findActiveDonation(id));
	}

	@Transactional
	public Long registerDonation(List<MultipartFile> images, DonationRequest request)
		throws IOException {
		Member member = userService.getCurMember()
								   .orElseThrow();

		Donation donation = request.toEntity();
		donation.addMember(member);

		registerTag(request, donation);
		registerImage(images, donation);

		return donationRepository
			.save(donation)
			.getId();
	}

	@Transactional
	public Long modifyDonation(Long id, DonationRequest request) {
		Member member = userService.getCurMember()
								   .orElseThrow();

		Donation donation = findActiveDonation(id);
		checkWriter(member, donation);

		donation.changeInfo(request);
		donationTagRepository.deleteAllByDonation(donation);
		registerTag(request, donation);

		return donation.getId();
	}

	@Transactional
	public Long modifyDealStatus(Long id, DealStatusRequest request) {
		Member member = userService.getCurMember()
								   .orElseThrow();

		Donation donation = findActiveDonation(id);
		checkWriter(member, donation);

		donation.changeStatus(DonationStatus.of(request.getStatus()));

		return donation.getId();
	}

	@Transactional
	public void removeDonation(Long id) {
		Member member = userService.getCurMember()
								   .orElseThrow();

		Donation donation = findActiveDonation(id);
		checkWriter(member, donation);

		donation.deleteEntity();
	}

	@Transactional(readOnly = true)
	public Donation findActiveDonation(Long id) {
		return donationRepository
			.findByIdAndIsDeletedFalse(id)
			.orElseThrow(() -> new NotFoundResourceException(ErrorCode.NOT_FOUND_DONATION));
	}

	private void registerTag(DonationRequest request, Donation donation) {
		for (Long tagId : request.getTags()) {
			ThemeTag themeTag = themeTagRepository.findById(tagId)
												  .get();
			donation.addTag(themeTag);
		}
	}

	private void registerImage(
		List<MultipartFile> images, Donation donation
	) throws IOException {
		for (MultipartFile image : images) {
			String imageUrl = uploadService.upload(image, DIRNAME);
			donation.addImage(
				DonationImage.registerImage(imageUrl, donation)
			);
		}
	}

	private void checkWriter(Member member, Donation donation) {
		if (!donation.getMember()
					 .equals(member)) {
			throw new NotMatchResourceException(ErrorCode.NOT_MATCH_WRITER);
		}
	}
}

