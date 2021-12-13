package com.prgrms.needit.domain.board.wish.service;

import com.prgrms.needit.common.domain.dto.CommentRequest;
import com.prgrms.needit.common.enums.UserType;
import com.prgrms.needit.common.error.ErrorCode;
import com.prgrms.needit.common.error.exception.NotFoundResourceException;
import com.prgrms.needit.common.error.exception.NotMatchResourceException;
import com.prgrms.needit.domain.board.wish.entity.DonationWish;
import com.prgrms.needit.domain.board.wish.entity.DonationWishComment;
import com.prgrms.needit.domain.board.wish.repository.WishCommentRepository;
import com.prgrms.needit.domain.notification.entity.enums.NotificationContentType;
import com.prgrms.needit.domain.notification.service.NotificationService;
import com.prgrms.needit.domain.user.login.service.UserService;
import com.prgrms.needit.domain.user.member.entity.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WishCommentService {

	private final UserService userService;
	private final DonationWishService donationWishService;
	private final WishCommentRepository commentRepository;
	private final NotificationService notificationService;

	public WishCommentService(
		UserService userService,
		DonationWishService donationWishService,
		WishCommentRepository commentRepository,
		NotificationService notificationService
	) {
		this.userService = userService;
		this.donationWishService = donationWishService;
		this.commentRepository = commentRepository;
		this.notificationService = notificationService;
	}

	@Transactional
	public Long registerComment(Long id, CommentRequest request) {
		Member member = userService.getCurMember()
								   .orElseThrow();

		DonationWish wish = donationWishService.findActiveDonationWish(id);
		DonationWishComment wishComment = request.toEntity(member, wish);

		wish.addComment(wishComment);

		Long createdCommentId = commentRepository.save(wishComment)
									.getId();
		notificationService.createAndSendNotification(
			wish.getCenter().getEmail(),
			wish.getCenter().getId(),
			UserType.CENTER,
			NotificationContentType.WISH_COMMENT,
			createdCommentId,
			wishComment.getComment());
		return createdCommentId;
	}

	@Transactional
	public void removeComment(Long wishId, Long commentId) {
		Member member = userService.getCurMember()
								   .orElseThrow();

		DonationWish wish = donationWishService.findActiveDonationWish(wishId);
		DonationWishComment wishComment = findActiveComment(commentId);

		if (!wishComment.getDonationWish()
						.equals(wish)) {
			throw new NotMatchResourceException(ErrorCode.NOT_MATCH_COMMENT);
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
			throw new NotMatchResourceException(ErrorCode.NOT_MATCH_WRITER);
		}
	}

}
