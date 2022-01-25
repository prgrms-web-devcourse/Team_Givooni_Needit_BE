package com.prgrms.needit.domain.board.wish.repository;

import com.prgrms.needit.domain.board.wish.entity.DonationWish;
import com.prgrms.needit.domain.center.entity.Center;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DonationWishRepository
	extends JpaRepository<DonationWish, Long>, WishCustomRepository {

	Optional<DonationWish> findByIdAndIsDeletedFalse(Long id);

	List<DonationWish> findAllByCenterAndIsDeletedFalse(Center center);
}
