package com.prgrms.needit.domain.board.wish.repository;

import com.prgrms.needit.domain.board.wish.entity.DonationWish;
import com.prgrms.needit.domain.board.wish.entity.DonationWishComment;
import com.prgrms.needit.domain.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishCommentRepository extends JpaRepository<DonationWishComment, Long> {

	Optional<DonationWishComment> findByDonationWishAndMember(DonationWish wish, Member member);

	Optional<DonationWishComment> findByIdAndIsDeletedFalse(Long id);

	boolean existsByMemberAndDonationWish(Member member, DonationWish wish);
}
