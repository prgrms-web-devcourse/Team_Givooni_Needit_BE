package com.prgrms.needit.domain.board.donation.repository;

import com.prgrms.needit.domain.board.donation.entity.Donation;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DonationRepository extends JpaRepository<Donation, Long> {

	Optional<Donation> findByIdAndIsDeletedFalse(Long id);
}
