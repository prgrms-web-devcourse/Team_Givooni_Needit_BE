package com.prgrms.needit.domain.favorite.repository;

import com.prgrms.needit.domain.center.entity.Center;
import com.prgrms.needit.domain.favorite.entity.FavoriteCenter;
import com.prgrms.needit.domain.user.entity.Users;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteCenterRepository extends JpaRepository<FavoriteCenter, Long> {

	List<FavoriteCenter> findAllByMemberOrderByCreatedAt(Users member);

	List<FavoriteCenter> findAllByCenter(Center center);

	void deleteByMemberAndCenter(Users member, Users center);

	boolean existsByMemberAndCenter(Users member, Users center);

}
