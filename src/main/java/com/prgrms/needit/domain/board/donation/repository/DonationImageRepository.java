package com.prgrms.needit.domain.board.donation.repository;

import com.prgrms.needit.domain.board.donation.entity.Donation;
import com.prgrms.needit.domain.board.donation.entity.DonationImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DonationImageRepository extends JpaRepository<DonationImage, Long> {

	void deleteAllByDonation(Donation donation);
}
