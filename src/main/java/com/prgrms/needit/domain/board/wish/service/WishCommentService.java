package com.prgrms.needit.domain.board.wish.service;

import com.prgrms.needit.common.domain.dto.CommentRequest;
import com.prgrms.needit.common.error.ErrorCode;
import com.prgrms.needit.common.error.exception.NotFoundResourceException;
import com.prgrms.needit.common.error.exception.NotMatchCommentException;
import com.prgrms.needit.common.error.exception.NotMatchWriterException;
import com.prgrms.needit.domain.board.wish.entity.DonationWish;
import com.prgrms.needit.domain.board.wish.entity.DonationWishComment;
import com.prgrms.needit.domain.board.wish.repository.WishCommentRepository;
import com.prgrms.needit.domain.member.entity.Member;
import com.prgrms.needit.domain.user.login.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WishCommentService {

	private final UserService userService;
	private final DonationWishService donationWishService;
	private final WishCommentRepository commentRepository;

	public WishCommentService(
		UserService userService,
		DonationWishService donationWishService,
		WishCommentRepository commentRepository
	) {
		this.userService = userService;
		this.donationWishService = donationWishService;
		this.commentRepository = commentRepository;
	}

	@Transactional
	public Long registerComment(Long id, CommentRequest request) {
		Member member = (Member) userService.getCurUser();

		DonationWish wish = donationWishService.findActiveDonationWish(id);
		DonationWishComment wishComment = request.toEntity(member, wish);

		wish.addComment(wishComment);

		return commentRepository.save(wishComment)
								.getId();
	}

	@Transactional
	public void removeComment(Long wishId, Long commentId) {
		Member member = (Member) userService.getCurUser();

		DonationWish wish = donationWishService.findActiveDonationWish(wishId);
		DonationWishComment wishComment = findActiveComment(commentId);

		if (!wishComment.getDonationWish()
						.equals(wish)) {
			throw new NotMatchCommentException(ErrorCode.NOT_MATCH_COMMENT);
		}
		checkWriter(member, wishComment);

		wishComment.deleteEntity();
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
			throw new NotMatchWriterException(ErrorCode.NOT_MATCH_WRITER);
		}
	}

}
