package com.prgrms.needit.domain.board.wish.service;

import com.prgrms.needit.common.domain.dto.DealStatusRequest;
import com.prgrms.needit.common.domain.entity.ThemeTag;
import com.prgrms.needit.common.domain.repository.ThemeTagRepository;
import com.prgrms.needit.common.domain.service.UploadService;
import com.prgrms.needit.common.enums.DonationStatus;
import com.prgrms.needit.common.error.ErrorCode;
import com.prgrms.needit.common.error.exception.NotFoundResourceException;
import com.prgrms.needit.common.error.exception.NotMatchResourceException;
import com.prgrms.needit.domain.board.wish.dto.DonationWishFilterRequest;
import com.prgrms.needit.domain.board.wish.dto.DonationWishRequest;
import com.prgrms.needit.domain.board.wish.dto.DonationWishResponse;
import com.prgrms.needit.domain.board.wish.entity.DonationWish;
import com.prgrms.needit.domain.board.wish.entity.DonationWishImage;
import com.prgrms.needit.domain.board.wish.repository.DonationWishRepository;
import com.prgrms.needit.domain.board.wish.repository.DonationWishTagRepository;
import com.prgrms.needit.domain.user.center.entity.Center;
import com.prgrms.needit.domain.user.login.service.UserService;
import java.io.IOException;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DonationWishService {

	private static final String DIRNAME = "donation-wish";

	private final UserService userService;
	private final UploadService uploadService;
	private final DonationWishRepository donationWishRepository;
	private final DonationWishTagRepository donationWishTagRepository;
	private final ThemeTagRepository themeTagRepository;

	public DonationWishService(
		UserService userService,
		UploadService uploadService,
		DonationWishRepository donationWishRepository,
		DonationWishTagRepository donationWishTagRepository,
		ThemeTagRepository themeTagRepository
	) {
		this.userService = userService;
		this.uploadService = uploadService;
		this.donationWishRepository = donationWishRepository;
		this.donationWishTagRepository = donationWishTagRepository;
		this.themeTagRepository = themeTagRepository;
	}

	@Transactional(readOnly = true)
	public Page<DonationWishResponse> getDonationWishes(
		DonationWishFilterRequest request, Pageable pageable
	) {
		return donationWishRepository.searchAllByFilter(request, pageable)
									 .map(DonationWishResponse::new);
	}

	@Transactional(readOnly = true)
	public DonationWishResponse getDonationWish(Long id) {
		return new DonationWishResponse(findActiveDonationWish(id));
	}

	@Transactional
	public Long registerDonationWish(
		List<MultipartFile> images, DonationWishRequest request
	) throws IOException {
		Center center = userService.getCurCenter()
								   .orElseThrow();

		DonationWish wish = request.toEntity();
		wish.addCenter(center);

		registerTag(request, wish);
		registerImage(images, wish);

		return donationWishRepository
			.save(wish)
			.getId();
	}

	@Transactional
	public Long modifyDonationWish(Long id, DonationWishRequest request) {
		Center center = userService.getCurCenter()
								   .orElseThrow();

		DonationWish wish = findActiveDonationWish(id);
		checkWriter(center, wish);

		wish.changeInfo(request);
		donationWishTagRepository.deleteAllByDonationWish(wish);
		registerTag(request, wish);

		return wish.getId();
	}

	@Transactional
	public Long modifyDealStatus(Long id, DealStatusRequest request) {
		Center center = userService.getCurCenter()
								   .orElseThrow();

		DonationWish wish = findActiveDonationWish(id);
		checkWriter(center, wish);

		wish.changeStatus(DonationStatus.of(request.getStatus()));

		return wish.getId();
	}

	@Transactional
	public void removeDonationWish(Long id) {
		Center center = userService.getCurCenter()
								   .orElseThrow();

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

	private void registerImage(
		List<MultipartFile> images, DonationWish wish
	) throws IOException {
		for (MultipartFile image : images) {
			String imageUrl = uploadService.upload(image, DIRNAME);
			wish.addImage(
				DonationWishImage.registerImage(imageUrl, wish)
			);
		}
	}

	private void checkWriter(Center center, DonationWish wish) {
		if (!wish.getCenter()
				 .equals(center)) {
			throw new NotMatchResourceException(ErrorCode.NOT_MATCH_WRITER);
		}
	}
}
