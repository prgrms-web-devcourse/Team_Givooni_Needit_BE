package com.prgrms.needit.domain.board.donation.repository;

import com.prgrms.needit.domain.board.donation.entity.DonationImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface DonationImageRepository extends JpaRepository<DonationImage, Long> {

	@Transactional
	@Modifying
	@Query(value = "delete from donation_image where donation_id = ?1", nativeQuery = true)
	void deleteAllByDonation(Long donationId);
}
