package com.prgrms.needit.domain.board.wish.service;

import com.prgrms.needit.common.domain.entity.ThemeTag;
import com.prgrms.needit.common.domain.repository.ThemeTagRepository;
import com.prgrms.needit.domain.board.wish.dto.DonationWishRequest;
import com.prgrms.needit.domain.board.wish.entity.DonationWish;
import com.prgrms.needit.domain.board.wish.repository.DonationWishRepository;
import com.prgrms.needit.domain.center.entity.Center;
import com.prgrms.needit.domain.user.login.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DonationWishService {

	private final UserService userService;
	private final DonationWishRepository donationWishRepository;
	private final ThemeTagRepository themeTagRepository;

	public DonationWishService(
		UserService userService,
		DonationWishRepository donationWishRepository,
		ThemeTagRepository themeTagRepository
	) {
		this.userService = userService;
		this.donationWishRepository = donationWishRepository;
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

	private void registerTag(DonationWishRequest request, DonationWish wish) {
		for (Long tagId : request.getTags()) {
			ThemeTag themeTag = themeTagRepository.findById(tagId)
												  .get();
			wish.addTag(themeTag);
		}
	}
}
