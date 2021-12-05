package com.prgrms.needit.domain.board.donation.service;

import com.prgrms.needit.common.domain.dto.CommentRequest;
import com.prgrms.needit.common.error.ErrorCode;
import com.prgrms.needit.common.error.exception.NotFoundResourceException;
import com.prgrms.needit.common.error.exception.NotMatchCommentException;
import com.prgrms.needit.common.error.exception.NotMatchWriterException;
import com.prgrms.needit.domain.board.donation.entity.Donation;
import com.prgrms.needit.domain.board.donation.entity.DonationComment;
import com.prgrms.needit.domain.board.donation.repository.CommentRepository;
import com.prgrms.needit.domain.center.entity.Center;
import com.prgrms.needit.domain.center.repository.CenterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {

	private final CenterRepository centerRepository;
	private final DonationService donationService;
	private final CommentRepository commentRepository;

	public CommentService(
		CenterRepository centerRepository,
		DonationService donationService,
		CommentRepository commentRepository
	) {
		this.centerRepository = centerRepository;
		this.donationService = donationService;
		this.commentRepository = commentRepository;
	}

	@Transactional
	public Long registerComment(Long id, CommentRequest request) {
		Center center = centerRepository.findById(1L)
										.get();
		Donation donation = donationService.findActiveDonation(id);
		DonationComment donationComment = request.toEntity(center, donation);

		donation.addComment(donationComment);

		return commentRepository.save(donationComment)
								.getId();
	}

	@Transactional
	public void removeComment(Long donationId, Long commentId) {
		Center center = centerRepository.findById(1L)
										.get();
		Donation donation = donationService.findActiveDonation(donationId);
		DonationComment comment = findActiveComment(commentId);

		if (!comment.getDonation()
					.equals(donation)) {
			throw new NotMatchCommentException(ErrorCode.NOT_MATCH_COMMENT);
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
			throw new NotMatchWriterException(ErrorCode.NOT_MATCH_WRITER);
		}
	}

}
