package com.prgrms.needit.domain.board.wish.service;

import com.prgrms.needit.common.domain.dto.DealStatusRequest;
import com.prgrms.needit.common.domain.entity.ThemeTag;
import com.prgrms.needit.common.domain.repository.ThemeTagRepository;
import com.prgrms.needit.common.enums.DonationStatus;
import com.prgrms.needit.common.error.ErrorCode;
import com.prgrms.needit.common.error.exception.NotFoundResourceException;
import com.prgrms.needit.common.error.exception.NotMatchWriterException;
import com.prgrms.needit.domain.board.wish.dto.DonationWishRequest;
import com.prgrms.needit.domain.board.wish.entity.DonationWish;
import com.prgrms.needit.domain.board.wish.repository.DonationWishRepository;
import com.prgrms.needit.domain.board.wish.repository.DonationWishTagRepository;
import com.prgrms.needit.domain.center.entity.Center;
import com.prgrms.needit.domain.user.login.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DonationWishService {

	private final UserService userService;
	private final DonationWishRepository donationWishRepository;
	private final DonationWishTagRepository donationWishTagRepository;
	private final ThemeTagRepository themeTagRepository;

	public DonationWishService(
		UserService userService,
		DonationWishRepository donationWishRepository,
		DonationWishTagRepository donationWishTagRepository,
		ThemeTagRepository themeTagRepository
	) {
		this.userService = userService;
		this.donationWishRepository = donationWishRepository;
		this.donationWishTagRepository = donationWishTagRepository;
		this.themeTagRepository = themeTagRepository;
	}

	@Transactional
	public Long registerDonationWish(DonationWishRequest request) {
		Center center = (Center) userService.getCurUser();

		DonationWish wish = request.toEntity();
		wish.addCenter(center);

		registerTag(request, wish);

		return donationWishRepository
			.save(wish)
			.getId();
	}

	@Transactional
	public Long modifyDonationWish(Long id, DonationWishRequest request) {
		Center center = (Center) userService.getCurUser();

		DonationWish wish = findActiveDonationWish(id);
		checkWriter(center, wish);

		wish.changeInfo(request);
		donationWishTagRepository.deleteAllByDonationWish(wish);
		registerTag(request, wish);

		return wish.getId();
	}

	@Transactional
	public Long modifyDealStatus(Long id, DealStatusRequest request) {
		Center center = (Center) userService.getCurUser();

		DonationWish wish = findActiveDonationWish(id);
		checkWriter(center, wish);

		wish.changeStatus(DonationStatus.of(request.getStatus()));

		return wish.getId();
	}

	@Transactional
	public void removeDonationWish(Long id) {
		Center center = (Center) userService.getCurUser();

		DonationWish wish = findActiveDonationWish(id);
		checkWriter(center, wish);

		wish.deleteEntity();
	}

	@Transactional(readOnly = true)
	public DonationWish findActiveDonationWish(Long id) {
		return donationWishRepository
			.findByIdAndIsDeletedFalse(id)
			.orElseThrow(() -> new NotFoundResourceException(ErrorCode.NOT_FOUND_DONATION_WISH));
	}

	private void registerTag(DonationWishRequest request, DonationWish wish) {
		for (Long tagId : request.getTags()) {
			ThemeTag themeTag = themeTagRepository.findById(tagId)
												  .get();
			wish.addTag(themeTag);
		}
	}

	private void checkWriter(Center center, DonationWish wish) {
		if (!wish.getCenter()
				 .equals(center)) {
			throw new NotMatchWriterException(ErrorCode.NOT_MATCH_WRITER);
		}
	}
}
