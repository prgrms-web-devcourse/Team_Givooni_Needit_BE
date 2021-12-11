package com.prgrms.needit.domain.board.wish.repository;

import com.prgrms.needit.domain.board.wish.entity.DonationWish;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DonationWishRepository extends JpaRepository<DonationWish, Long> {

	Optional<DonationWish> findByIdAndIsDeletedFalse(Long id);
}
