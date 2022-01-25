package com.prgrms.needit.domain.favorite.repository;

import com.prgrms.needit.domain.center.entity.Center;
import com.prgrms.needit.domain.favorite.entity.FavoriteCenter;
import com.prgrms.needit.domain.member.entity.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteCenterRepository extends JpaRepository<FavoriteCenter, Long> {

	void deleteByMemberAndCenter(Member member, Center centerId);

	List<FavoriteCenter> findAllByMemberOrderByCreatedAt(Member member);

	Optional<FavoriteCenter> findByMemberAndCenter(Member member, Center center);

	List<FavoriteCenter> findAllByCenter(Center center);
}
