package com.prgrms.needit.domain.user.favorite.repository;

import com.prgrms.needit.domain.user.center.entity.Center;
import com.prgrms.needit.domain.user.favorite.entity.FavoriteCenter;
import com.prgrms.needit.domain.user.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteCenterRepository extends JpaRepository<FavoriteCenter, Long> {

	void deleteByMemberAndCenter(Member member, Center centerId);
}
