package com.prgrms.needit.domain.message.repository;

import com.prgrms.needit.domain.board.donation.entity.DonationComment;
import com.prgrms.needit.domain.board.wish.entity.DonationWishComment;
import com.prgrms.needit.domain.message.entity.ChatMessage;
import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ChatMessageRepository extends
	PagingAndSortingRepository<ChatMessage, Long> {

	List<ChatMessage> findFirst100ByDonationCommentAndIdGreaterThan(
		DonationComment donationComment,
		long messageId
	);

	List<ChatMessage> findFirst100ByDonationWishCommentAndIdGreaterThan(
		DonationWishComment donationWishComment,
		long messageId
	);

}
