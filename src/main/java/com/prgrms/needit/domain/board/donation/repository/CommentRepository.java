package com.prgrms.needit.domain.board.donation.repository;

import com.prgrms.needit.domain.board.donation.entity.Donation;
import com.prgrms.needit.domain.board.donation.entity.DonationComment;
import com.prgrms.needit.domain.user.center.entity.Center;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<DonationComment, Long> {

	Optional<DonationComment> findByDonationAndCenter(Donation donation, Center center);

	Optional<DonationComment> findByIdAndIsDeletedFalse(Long id);

	boolean existsByCenterAndDonation(Center center, Donation donation);
}
