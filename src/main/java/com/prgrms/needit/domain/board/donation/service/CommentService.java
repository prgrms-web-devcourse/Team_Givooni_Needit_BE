package com.prgrms.needit.domain.board.donation.service;

import com.prgrms.needit.common.domain.dto.CommentRequest;
import com.prgrms.needit.common.error.ErrorCode;
import com.prgrms.needit.common.error.exception.NotFoundResourceException;
import com.prgrms.needit.common.error.exception.NotMatchResourceException;
import com.prgrms.needit.domain.board.donation.entity.Donation;
import com.prgrms.needit.domain.board.donation.entity.DonationComment;
import com.prgrms.needit.domain.board.donation.repository.CommentRepository;
import com.prgrms.needit.domain.board.donation.repository.DonationRepository;
import com.prgrms.needit.domain.user.center.entity.Center;
import com.prgrms.needit.domain.user.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

	private final UserService userService;
	private final DonationRepository donationRepository;
	private final CommentRepository commentRepository;

	@Transactional
	public Long registerComment(Long id, CommentRequest request) {
		Center center = userService.getCurCenter()
								   .orElseThrow();

		Donation donation = findActiveDonation(id);
		DonationComment donationComment = request.toEntity(center, donation);

		donation.addComment(donationComment);

		return commentRepository.save(donationComment)
								.getId();
	}

	@Transactional
	public void removeComment(Long donationId, Long commentId) {
		Center center = userService.getCurCenter()
								   .orElseThrow();

		Donation donation = findActiveDonation(donationId);
		DonationComment comment = findActiveComment(commentId);

		if (!comment.getDonation()
					.equals(donation)) {
			throw new NotMatchResourceException(ErrorCode.NOT_MATCH_COMMENT);
		}
		checkWriter(center, comment);

		comment.deleteEntity();
	}

	@Transactional(readOnly = true)
	public Donation findActiveDonation(Long id) {
		return donationRepository
			.findByIdAndIsDeletedFalse(id)
			.orElseThrow(() -> new NotFoundResourceException(ErrorCode.NOT_FOUND_DONATION));
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
