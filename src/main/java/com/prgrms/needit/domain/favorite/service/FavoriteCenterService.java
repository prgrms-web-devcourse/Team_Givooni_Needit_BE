package com.prgrms.needit.domain.favorite.service;

import com.prgrms.needit.common.enums.UserType;
import com.prgrms.needit.common.error.ErrorCode;
import com.prgrms.needit.common.error.exception.DuplicatedResourceException;
import com.prgrms.needit.common.error.exception.NotFoundResourceException;
import com.prgrms.needit.domain.favorite.entity.FavoriteCenter;
import com.prgrms.needit.domain.favorite.repository.FavoriteCenterRepository;
import com.prgrms.needit.domain.user.entity.Users;
import com.prgrms.needit.domain.user.repository.UserRepository;
import com.prgrms.needit.domain.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FavoriteCenterService {

	private final UserRepository userRepository;
	private final FavoriteCenterRepository favCenterRepository;
	private final AuthService authService;

	@Transactional
	public Long addFavoriteCenter(Long centerId) {
		Users curUser = authService.getCurUser();
		Users center = findActiveCenter(centerId);

		if (isFavCenterExist(curUser, center)) {
			throw new DuplicatedResourceException(ErrorCode.ALREADY_EXIST_FAVCENTER);
		}

		return favCenterRepository.save(FavoriteCenter.createFavCenter(curUser, center))
								  .getId();
	}

	@Transactional
	public void removeFavoriteCenter(Long centerId) {
		Users curUser = authService.getCurUser();
		Users center = findActiveCenter(centerId);

		favCenterRepository.deleteByMemberAndCenter(curUser, center);
	}

	private Users findActiveCenter(Long centerId) {
		return userRepository.findByIdAndIsDeletedFalse(centerId)
							 .filter(users -> users.getUserRole()
												   .equals(UserType.CENTER))
							 .orElseThrow(
								 () -> new NotFoundResourceException(ErrorCode.NOT_FOUND_CENTER));
	}

	private boolean isFavCenterExist(Users member, Users center) {
		return favCenterRepository.existsByMemberAndCenter(
			member, center);
	}

}
