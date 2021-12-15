package com.prgrms.needit.domain.user.favorite.repository;

import com.prgrms.needit.domain.user.favorite.entity.FavoriteCenter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteCenterRepository extends JpaRepository<FavoriteCenter, Long> {

	void deleteByCenter(Long centerId);
}
