package com.prgrms.needit.domain.user.member.repository;

import com.prgrms.needit.domain.user.member.entity.FavoriteCenter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteCenterRepository extends JpaRepository<FavoriteCenter, Long> {

	void deleteByCenter(Long centerId);
}
