package com.prgrms.needit.domain.board.donation.repository;

import com.prgrms.needit.domain.board.donation.entity.Donation;
import com.prgrms.needit.domain.board.donation.entity.DonationHaveTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DonationTagRepository extends JpaRepository<DonationHaveTag, Long> {

	void deleteAllByDonation(Donation donation);
}
