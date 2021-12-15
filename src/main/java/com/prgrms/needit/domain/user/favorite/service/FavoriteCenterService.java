package com.prgrms.needit.domain.user.favorite.service;

import com.prgrms.needit.common.error.ErrorCode;
import com.prgrms.needit.common.error.exception.NotFoundResourceException;
import com.prgrms.needit.domain.user.center.dto.CenterListResponse;
import com.prgrms.needit.domain.user.center.entity.Center;
import com.prgrms.needit.domain.user.center.repository.CenterRepository;
import com.prgrms.needit.domain.user.favorite.entity.FavoriteCenter;
import com.prgrms.needit.domain.user.favorite.repository.FavoriteCenterRepository;
import com.prgrms.needit.domain.user.member.entity.Member;
import com.prgrms.needit.domain.user.user.service.UserService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FavoriteCenterService {

	private final CenterRepository centerRepository;
	private final FavoriteCenterRepository favoriteCenterRepository;
	private final UserService userService;

	public FavoriteCenterService(
		CenterRepository centerRepository,
		FavoriteCenterRepository favoriteCenterRepository,
		UserService userService
	) {
		this.centerRepository = centerRepository;
		this.favoriteCenterRepository = favoriteCenterRepository;
		this.userService = userService;
	}

	@Transactional(readOnly = true)
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


	@Transactional
	public Long addFavoriteCenter(Long centerId) {
		Member curMember = userService.getCurMember()
									  .orElseThrow();
		Center center = findActiveCenter(centerId);

		curMember.addFavCenter(center);

		FavoriteCenter favCenter = FavoriteCenter.createFavCenter(curMember, center);

		return favCenter.getId();
	}


	public Center findActiveCenter(Long centerId) {
		return centerRepository
			.findByIdAndIsDeletedFalse(centerId)
			.orElseThrow(
				() -> new NotFoundResourceException(ErrorCode.NOT_FOUND_CENTER));
	}

}
