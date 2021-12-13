package com.prgrms.needit.domain.board.wish.repository;

import com.prgrms.needit.domain.board.wish.entity.DonationWishImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface DonationWishImageRepository extends JpaRepository<DonationWishImage, Long> {

	@Transactional
	@Modifying
	@Query(value = "delete from wish_image where donation_wish_id = ?1", nativeQuery = true)
	void deleteAllByDonation(Long donationWishId);
}
