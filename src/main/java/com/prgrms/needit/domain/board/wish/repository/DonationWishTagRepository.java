package com.prgrms.needit.domain.board.wish.repository;

import com.prgrms.needit.domain.board.wish.entity.DonationWish;
import com.prgrms.needit.domain.board.wish.entity.DonationWishHaveTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DonationWishTagRepository extends JpaRepository<DonationWishHaveTag, Long> {

	void deleteAllByDonationWish(DonationWish wish);
}
