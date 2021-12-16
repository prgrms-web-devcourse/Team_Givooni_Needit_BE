package com.prgrms.needit.domain.user.favorite.service;

import com.prgrms.needit.common.error.ErrorCode;
import com.prgrms.needit.common.error.exception.ExistResourceException;
import com.prgrms.needit.common.error.exception.NotFoundResourceException;
import com.prgrms.needit.domain.user.center.dto.CentersResponse;
import com.prgrms.needit.domain.user.center.entity.Center;
import com.prgrms.needit.domain.user.center.repository.CenterRepository;
import com.prgrms.needit.domain.user.favorite.entity.FavoriteCenter;
import com.prgrms.needit.domain.user.favorite.repository.FavoriteCenterRepository;
import com.prgrms.needit.domain.user.member.entity.Member;
import com.prgrms.needit.domain.user.user.service.UserService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FavoriteCenterService {

	private final CenterRepository centerRepository;
	private final FavoriteCenterRepository favCenterRepository;
	private final UserService userService;

	public FavoriteCenterService(
		CenterRepository centerRepository,
		FavoriteCenterRepository favoriteCenterRepository,
		UserService userService
	) {
		this.centerRepository = centerRepository;
		this.favCenterRepository = favoriteCenterRepository;
		this.userService = userService;
	}

	@Transactional(readOnly = true)
	public List<CentersResponse> getFavCenters() {
		Member curMember = userService.getCurMember()
									  .orElseThrow();
		List<FavoriteCenter> favCenters = favCenterRepository.findAllByMemberOrderByCreatedAt(
			curMember);
		return favCenters.stream()
						 .map(FavoriteCenter::getCenter)
						 .map(CentersResponse::new)
						 .collect(Collectors.toList());
	}

	@Transactional
	public Long addFavoriteCenter(Long centerId) {
		Member curMember = userService.getCurMember()
									  .orElseThrow();
		Center center = findActiveCenter(centerId);

		if (isFavCenterExist(curMember, center)) {
			throw new ExistResourceException(ErrorCode.ALREADY_EXIST_FAVCENTER);
		}

		curMember.addFavCenter(center);

		FavoriteCenter favCenter = FavoriteCenter.createFavCenter(curMember, center);
		return favCenterRepository.save(favCenter)
								  .getId();

	}

	@Transactional
	public void removeFavoriteCenter(Long centerId) {

		Member member = userService.getCurMember()
								   .orElseThrow();
		Center center = findActiveCenter(centerId);

		member.deleteFavCenter(center);
		favCenterRepository.deleteByMemberAndCenter(member, center);
	}


	public Center findActiveCenter(Long centerId) {
		return centerRepository
			.findByIdAndIsDeletedFalse(centerId)
			.orElseThrow(
				() -> new NotFoundResourceException(ErrorCode.NOT_FOUND_CENTER));
	}

	public boolean isFavCenterExist(Member member, Center center) {
		Optional<FavoriteCenter> favCenter = favCenterRepository.findByMemberAndCenter(
			member, center);
		return favCenter.isPresent();
	}

}
