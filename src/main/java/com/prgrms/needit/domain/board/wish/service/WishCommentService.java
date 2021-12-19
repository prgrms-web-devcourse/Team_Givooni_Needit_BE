package com.prgrms.needit.domain.board.wish.service;

import com.prgrms.needit.common.domain.dto.CommentRequest;
import com.prgrms.needit.common.enums.UserType;
import com.prgrms.needit.common.error.ErrorCode;
import com.prgrms.needit.common.error.exception.ExistResourceException;
import com.prgrms.needit.common.error.exception.NotFoundResourceException;
import com.prgrms.needit.common.error.exception.NotMatchResourceException;
import com.prgrms.needit.domain.board.wish.entity.DonationWish;
import com.prgrms.needit.domain.board.wish.entity.DonationWishComment;
import com.prgrms.needit.domain.board.wish.repository.DonationWishRepository;
import com.prgrms.needit.domain.board.wish.repository.WishCommentRepository;
import com.prgrms.needit.domain.notification.entity.enums.NotificationContentType;
import com.prgrms.needit.domain.notification.service.NotificationService;
import com.prgrms.needit.domain.user.member.entity.Member;
import com.prgrms.needit.domain.user.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WishCommentService {

	private final UserService userService;
	private final NotificationService notificationService;
	private final DonationWishRepository donationWishRepository;
	private final WishCommentRepository commentRepository;

	@Transactional
	public Long registerComment(Long id, CommentRequest request) {
		Member member = userService.getCurMember()
								   .orElseThrow();

		DonationWish wish = findActiveDonationWish(id);
		isCommentExist(member, wish);

		DonationWishComment wishComment = request.toEntity(member, wish);
		wish.addComment(wishComment);
		Long commentId = commentRepository.save(wishComment)
										  .getId();

		notificationService.createAndSendNotification(
			member.getEmail(),
			member.getId(),
			UserType.MEMBER,
			NotificationContentType.WISH,
			wish.getId(),
			member.getNickname() + "님이 센터의 기부를 희망하고 있어요!"
		);

		return commentId;
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

	private void isCommentExist(Member member, DonationWish wish) {
		boolean isExist = commentRepository.existsByMemberAndDonationWish(member, wish);

		if (isExist) {
			throw new ExistResourceException(ErrorCode.ALREADY_EXIST_COMMENT);
		}
	}

}
