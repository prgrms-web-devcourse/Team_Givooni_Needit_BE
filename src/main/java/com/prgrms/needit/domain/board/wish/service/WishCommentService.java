package com.prgrms.needit.domain.board.wish.service;

import com.prgrms.needit.common.domain.dto.CommentRequest;
import com.prgrms.needit.common.error.ErrorCode;
import com.prgrms.needit.common.error.exception.NotFoundResourceException;
import com.prgrms.needit.common.error.exception.NotMatchResourceException;
import com.prgrms.needit.domain.board.wish.entity.DonationWish;
import com.prgrms.needit.domain.board.wish.entity.DonationWishComment;
import com.prgrms.needit.domain.board.wish.repository.DonationWishRepository;
import com.prgrms.needit.domain.board.wish.repository.WishCommentRepository;
import com.prgrms.needit.domain.user.user.service.UserService;
import com.prgrms.needit.domain.user.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WishCommentService {

	private final UserService userService;
	private final DonationWishRepository donationWishRepository;
	private final WishCommentRepository commentRepository;

	@Transactional
	public Long registerComment(Long id, CommentRequest request) {
		Member member = userService.getCurMember()
								   .orElseThrow();

		DonationWish wish = findActiveDonationWish(id);
		DonationWishComment wishComment = request.toEntity(member, wish);

		wish.addComment(wishComment);

		return commentRepository.save(wishComment)
								.getId();
	}

	@Transactional
	public void removeComment(Long wishId, Long commentId) {
		Member member = userService.getCurMember()
								   .orElseThrow();

		DonationWish wish = findActiveDonationWish(wishId);
		DonationWishComment wishComment = findActiveComment(commentId);

		if (!wishComment.getDonationWish()
						.equals(wish)) {
			throw new NotMatchResourceException(ErrorCode.NOT_MATCH_COMMENT);
		}
		checkWriter(member, wishComment);

		wishComment.deleteEntity();
	}

	@Transactional(readOnly = true)
	public DonationWish findActiveDonationWish(Long id) {
		return donationWishRepository
			.findByIdAndIsDeletedFalse(id)
			.orElseThrow(() -> new NotFoundResourceException(ErrorCode.NOT_FOUND_DONATION_WISH));
	}

	@Transactional(readOnly = true)
	public DonationWishComment findActiveComment(Long id) {
		return commentRepository
			.findByIdAndIsDeletedFalse(id)
			.orElseThrow(() -> new NotFoundResourceException(ErrorCode.NOT_FOUND_APPLY_COMMENT));
	}

	private void checkWriter(Member member, DonationWishComment comment) {
		if (!comment.getMember()
					.equals(member)) {
			throw new NotMatchResourceException(ErrorCode.NOT_MATCH_WRITER);
		}
	}

}
