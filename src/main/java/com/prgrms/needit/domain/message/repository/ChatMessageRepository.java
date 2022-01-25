package com.prgrms.needit.domain.message.repository;

import com.prgrms.needit.domain.board.donation.entity.Donation;
import com.prgrms.needit.domain.board.wish.entity.DonationWish;
import com.prgrms.needit.domain.center.entity.Center;
import com.prgrms.needit.domain.member.entity.Member;
import com.prgrms.needit.domain.message.entity.ChatMessage;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ChatMessageRepository extends
	PagingAndSortingRepository<ChatMessage, Long> {

	@Query("SELECT m FROM ChatMessage m WHERE m.id in ("
		+ "select max(m.id) from ChatMessage m where m.member=?1 AND m.donation is not null GROUP BY m.donation)")
	List<ChatMessage> findDonationMessagesOfMemberAsGroup(Member member);

	@Query("SELECT m FROM ChatMessage m WHERE m.id in ("
		+ "select max(m.id) from ChatMessage m where m.member=?1 AND m.donationWish is not null GROUP BY m.donationWish)")
	List<ChatMessage> findDonationWishMessagesOfMemberAsGroup(Member member);

	@Query("SELECT m FROM ChatMessage m WHERE m.id in ("
		+ "select max(m.id) from ChatMessage m where m.center=?1 AND m.donation is not null GROUP BY m.donation)")
	List<ChatMessage> findDonationMessagesOfCenterAsGroup(Center center);

	@Query("SELECT m FROM ChatMessage m WHERE m.id in ("
		+ "select max(m.id) from ChatMessage m where m.center=?1 AND m.donationWish is not null GROUP BY m.donationWish)")
	List<ChatMessage> findDonationWishMessagesOfCenterAsGroup(Center center);

	List<ChatMessage> findFirst100ByDonationAndMemberAndCenterAndIdGreaterThan(
		Donation donation,
		Member member,
		Center center,
		Long messageId
	);

	List<ChatMessage> findFirst100ByDonationWishAndMemberAndCenterAndIdGreaterThan(
		DonationWish donationWish,
		Member member,
		Center center,
		Long messageId
	);

}
