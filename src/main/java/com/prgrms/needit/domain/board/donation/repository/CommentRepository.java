package com.prgrms.needit.domain.board.donation.repository;

import com.prgrms.needit.domain.board.donation.entity.DonationComment;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<DonationComment, Long> {

	Optional<DonationComment> findByIdAndIsDeletedFalse(Long id);
}
