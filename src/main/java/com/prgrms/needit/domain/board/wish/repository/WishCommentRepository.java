package com.prgrms.needit.domain.board.wish.repository;

import com.prgrms.needit.domain.board.wish.entity.DonationWishComment;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishCommentRepository extends JpaRepository<DonationWishComment, Long> {

	Optional<DonationWishComment> findByIdAndIsDeletedFalse(Long id);
}
