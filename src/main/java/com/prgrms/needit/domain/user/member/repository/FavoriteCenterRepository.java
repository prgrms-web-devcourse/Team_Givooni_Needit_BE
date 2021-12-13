package com.prgrms.needit.domain.user.member.repository;

import com.prgrms.needit.domain.user.center.entity.Center;
import com.prgrms.needit.domain.user.member.entity.FavoriteCenter;
import com.prgrms.needit.domain.user.member.entity.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteCenterRepository extends JpaRepository<FavoriteCenter, Long> {

	List<FavoriteCenter> findAllByMember(Member member);

	List<FavoriteCenter> findAllByCenter(Center center);

}
