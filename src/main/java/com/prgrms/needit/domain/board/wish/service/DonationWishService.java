package com.prgrms.needit.domain.board.wish.service;

import com.prgrms.needit.common.domain.dto.DealStatusRequest;
import com.prgrms.needit.common.domain.dto.DonationsResponse;
import com.prgrms.needit.common.domain.entity.ThemeTag;
import com.prgrms.needit.common.domain.repository.ThemeTagRepository;
import com.prgrms.needit.common.domain.service.UploadService;
import com.prgrms.needit.common.enums.DonationStatus;
import com.prgrms.needit.common.enums.UserType;
import com.prgrms.needit.common.error.ErrorCode;
import com.prgrms.needit.common.error.exception.NotFoundResourceException;
import com.prgrms.needit.common.error.exception.NotMatchResourceException;
import com.prgrms.needit.domain.board.wish.dto.DonationWishFilterRequest;
import com.prgrms.needit.domain.board.wish.dto.DonationWishRequest;
import com.prgrms.needit.domain.board.wish.dto.DonationWishResponse;
import com.prgrms.needit.domain.board.wish.entity.DonationWish;
import com.prgrms.needit.domain.board.wish.entity.DonationWishImage;
import com.prgrms.needit.domain.board.wish.repository.DonationWishImageRepository;
import com.prgrms.needit.domain.board.wish.repository.DonationWishRepository;
import com.prgrms.needit.domain.board.wish.repository.DonationWishTagRepository;
import com.prgrms.needit.domain.notification.entity.enums.NotificationContentType;
import com.prgrms.needit.domain.notification.service.NotificationService;
import com.prgrms.needit.domain.user.center.entity.Center;
import com.prgrms.needit.domain.user.favorite.entity.FavoriteCenter;
import com.prgrms.needit.domain.user.favorite.repository.FavoriteCenterRepository;
import com.prgrms.needit.domain.user.user.service.UserService;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class DonationWishService {

	private static final String DIRNAME = "donation-wish";

	private final UserService userService;
	private final UploadService uploadService;
	private final NotificationService notificationService;
	private final ThemeTagRepository themeTagRepository;
	private final DonationWishRepository donationWishRepository;
	private final DonationWishTagRepository donationWishTagRepository;
	private final DonationWishImageRepository donationWishImageRepository;
	private final FavoriteCenterRepository favoriteCenterRepository;

	@Transactional(readOnly = true)
	public Page<DonationsResponse> getDonationWishes(
		DonationWishFilterRequest request, Pageable pageable
	) {
		return donationWishRepository.searchAllByFilter(request, pageable)
									 .map(DonationsResponse::toResponse);
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

		Long wishId = donationWishRepository.save(wish)
											.getId();

		List<FavoriteCenter> favoriteCenters = favoriteCenterRepository.findAllByCenter(center);
		for (FavoriteCenter fav : favoriteCenters) {
			notificationService.createAndSendNotification(
				fav.getMember()
				   .getEmail(),
				fav.getMember()
				   .getId(),
				UserType.MEMBER,
				NotificationContentType.WISH,
				wish.getId(),
				center.getName() + "의 새로운 글이 등록되었어요!"
			);
		}

		return wishId;
	}

	@Transactional
	public Long modifyDonationWish(
		Long id, List<MultipartFile> images, DonationWishRequest request
	) throws IOException {
		Center center = userService.getCurCenter()
								   .orElseThrow();

		DonationWish wish = findActiveDonationWish(id);
		checkWriter(center, wish);

		wish.changeInfo(request);
		donationWishTagRepository.deleteAllByDonationWish(wish);

		registerTag(request, wish);
		registerImage(images, wish);

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
		List<MultipartFile> newImages, DonationWish wish
	) throws IOException {
		if (!wish.getImages()
				 .isEmpty()) {
			List<String> curImages = wish.getImages()
										 .stream()
										 .map(DonationWishImage::getUrl)
										 .collect(Collectors.toList());

			uploadService.deleteImage(curImages, DIRNAME);
			wish.getImages()
				.clear();
			donationWishImageRepository.deleteAllByDonationWish(wish);
		}

		if (!"".equals(newImages.get(0)
								.getOriginalFilename()) || newImages.isEmpty()) {
			for (MultipartFile image : newImages) {
				String imageUrl = uploadService.upload(image, DIRNAME);
				wish.addImage(
					DonationWishImage.registerImage(imageUrl, wish)
				);
			}
		}
	}

	private void checkWriter(Center center, DonationWish wish) {
		if (!wish.getCenter()
				 .equals(center)) {
			throw new NotMatchResourceException(ErrorCode.NOT_MATCH_WRITER);
		}
	}
}
