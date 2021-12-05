package com.prgrms.needit.common.utils;

import com.prgrms.needit.domain.board.donation.entity.DonationComment;
import com.prgrms.needit.domain.board.donation.repository.DonationCommentRepository;
import com.prgrms.needit.domain.board.wish.entity.DonationWishComment;
import com.prgrms.needit.domain.board.wish.repository.DonationWishCommentRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EntityFinder {
	private static final String DONATION_COMMENT_NOT_FOUND = "Donation comment with given id not found.";
	private static final String DONATION_WISH_COMMENT_NOT_FOUND = "Donation wish comment with given id not found.";

	// TODO: convert to donation's exception later.
	public static DonationComment findDonationComment(
		DonationCommentRepository donationCommentRepository,
		long donationArticleId,
		long donationCommentId
	) {
		DonationComment donationComment = donationCommentRepository
			.findById(donationCommentId)
			.orElseThrow(() -> new IllegalArgumentException(DONATION_COMMENT_NOT_FOUND));
		if (donationComment.getDonation()
						   .getId() != donationArticleId) {
			throw new IllegalArgumentException(DONATION_COMMENT_NOT_FOUND);
		}

		return donationComment;
	}

	// TODO: convert to donation's exception later.
	public static DonationWishComment findDonationWishComment(
		DonationWishCommentRepository donationWishCommentRepository,
		long donationWishArticleId,
		long donationWishCommentId
	) {
		DonationWishComment donationWishComment = donationWishCommentRepository
			.findById(donationWishCommentId)
			.orElseThrow(() -> new IllegalArgumentException(DONATION_WISH_COMMENT_NOT_FOUND));
		if (donationWishComment.getDonationWish()
							   .getId() != donationWishArticleId) {
			throw new IllegalArgumentException(DONATION_WISH_COMMENT_NOT_FOUND);
		}

		return donationWishComment;
	}

}
