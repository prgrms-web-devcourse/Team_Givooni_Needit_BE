package com.prgrms.needit.domain.board.donation.service;

import com.prgrms.needit.common.domain.dto.CommentRequest;
import com.prgrms.needit.common.enums.UserType;
import com.prgrms.needit.common.error.ErrorCode;
import com.prgrms.needit.common.error.exception.NotFoundResourceException;
import com.prgrms.needit.common.error.exception.NotMatchResourceException;
import com.prgrms.needit.domain.board.donation.entity.Donation;
import com.prgrms.needit.domain.board.donation.entity.DonationComment;
import com.prgrms.needit.domain.board.donation.repository.CommentRepository;
import com.prgrms.needit.domain.notification.entity.enums.NotificationContentType;
import com.prgrms.needit.domain.notification.service.NotificationService;
import com.prgrms.needit.domain.user.center.entity.Center;
import com.prgrms.needit.domain.user.login.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {

	private final UserService userService;
	private final DonationService donationService;
	private final CommentRepository commentRepository;
	private final NotificationService notificationService;

	public CommentService(
		UserService userService,
		DonationService donationService,
		CommentRepository commentRepository,
		NotificationService notificationService
	) {
		this.userService = userService;
		this.donationService = donationService;
		this.commentRepository = commentRepository;
		this.notificationService = notificationService;
	}

	@Transactional
	public Long registerComment(Long id, CommentRequest request) {
		Center center = userService.getCurCenter()
								   .orElseThrow();

		Donation donation = donationService.findActiveDonation(id);
		DonationComment donationComment = request.toEntity(center, donation);

		donation.addComment(donationComment);

		Long createdCommentId = commentRepository.save(donationComment)
									.getId();
		notificationService.createAndSendNotification(
			donation.getMember().getEmail(),
			donation.getMember().getId(),
			UserType.MEMBER,
			NotificationContentType.DONATION_COMMENT,
			createdCommentId,
			donationComment.getComment());
		return createdCommentId;
	}

	@Transactional
	public void removeComment(Long donationId, Long commentId) {
		Center center = userService.getCurCenter()
								   .orElseThrow();

		Donation donation = donationService.findActiveDonation(donationId);
		DonationComment comment = findActiveComment(commentId);

		if (!comment.getDonation()
					.equals(donation)) {
			throw new NotMatchResourceException(ErrorCode.NOT_MATCH_COMMENT);
		}
		checkWriter(center, comment);
		comment.deleteEntity();
	}

	@Transactional(readOnly = true)
	public DonationComment findActiveComment(Long id) {
		return commentRepository
			.findByIdAndIsDeletedFalse(id)
			.orElseThrow(() -> new NotFoundResourceException(ErrorCode.NOT_FOUND_WISH_COMMENT));
	}

	private void checkWriter(Center center, DonationComment comment) {
		if (!comment.getCenter()
					.equals(center)) {
			throw new NotMatchResourceException(ErrorCode.NOT_MATCH_WRITER);
		}
	}

}
