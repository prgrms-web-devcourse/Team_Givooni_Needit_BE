package com.prgrms.needit.domain.board.wish.repository;

import com.prgrms.needit.domain.board.wish.entity.DonationWish;
import com.prgrms.needit.domain.board.wish.entity.DonationWishImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DonationWishImageRepository extends JpaRepository<DonationWishImage, Long> {

	void deleteAllByDonationWish(DonationWish wish);
}
