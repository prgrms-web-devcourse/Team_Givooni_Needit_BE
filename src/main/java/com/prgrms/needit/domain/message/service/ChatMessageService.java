package com.prgrms.needit.domain.message.service;

import com.prgrms.needit.common.enums.UserType;
import com.prgrms.needit.domain.board.donation.entity.Donation;
import com.prgrms.needit.domain.board.donation.repository.DonationRepository;
import com.prgrms.needit.domain.board.wish.entity.DonationWish;
import com.prgrms.needit.domain.board.wish.repository.DonationWishRepository;
import com.prgrms.needit.domain.center.entity.Center;
import com.prgrms.needit.domain.center.repository.CenterRepository;
import com.prgrms.needit.domain.member.entity.Member;
import com.prgrms.needit.domain.member.repository.MemberRepository;
import com.prgrms.needit.domain.message.entity.ChatMessage;
import com.prgrms.needit.domain.message.entity.response.ChatMessageResponse;
import com.prgrms.needit.domain.message.repository.ChatMessageRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatMessageService {

	private final DonationRepository donationRepository;
	private final DonationWishRepository donationWishRepository;
	private final MemberRepository memberRepository;
	private final CenterRepository centerRepository;
	private final ChatMessageRepository chatMessageRepository;

	private Donation findDonation(Long donationId) {
		return donationRepository
			.findById(donationId)
			.orElseThrow(
				IllegalArgumentException::new); // TODO: change to donation not found.
	}

	private Member findMember(Long memberId) {
		return memberRepository
			.findById(memberId)
			.orElseThrow(
				IllegalArgumentException::new); // TODO: change to member not found.
	}

	private Center findCenter(Long centerId) {
		return centerRepository
			.findById(centerId)
			.orElseThrow(
				IllegalArgumentException::new); // TODO; change to center not found.
	}

	private DonationWish findDonationWish(Long donationWishId) {
		return donationWishRepository
			.findById(donationWishId)
			.orElseThrow(
				IllegalArgumentException::new); // TODO: change to donation wish not found.
	}

	/**
	 * Read chats of member grouped by donation, donation with article.
	 *
	 * @param memberId Member's id.
	 * @return Chats of member group by post.
	 */
	@Transactional(readOnly = true)
	public List<ChatMessageResponse> getMemberChats(Long memberId) {
		Member member = findMember(memberId);
		List<ChatMessage> donationMessagesAsGroup = chatMessageRepository
			.findDonationMessagesOfMemberAsGroup(member);
		List<ChatMessage> donationWishMessagesAsGroup = chatMessageRepository
			.findDonationWishMessagesOfMemberAsGroup(member);
		donationMessagesAsGroup.addAll(donationWishMessagesAsGroup);
		return donationMessagesAsGroup
			.stream()
			.map(ChatMessageResponse::new)
			.collect(Collectors.toList());
	}

	/**
	 * Read chats of center grouped by donation, donation with article.
	 *
	 * @param centerId Center's id.
	 * @return Chats of center group by post.
	 */
	@Transactional(readOnly = true)
	public List<ChatMessageResponse> getCenterChats(Long centerId) {
		Center center = findCenter(centerId);
		List<ChatMessage> donationMessagesAsGroup = chatMessageRepository
			.findDonationMessagesOfCenterAsGroup(center);
		List<ChatMessage> donationWishMessagesAsGroup = chatMessageRepository
			.findDonationWishMessagesOfCenterAsGroup(center);
		donationMessagesAsGroup.addAll(donationWishMessagesAsGroup);
		return donationMessagesAsGroup
			.stream()
			.map(ChatMessageResponse::new)
			.collect(Collectors.toList());
	}

	/**
	 * Get 100 chat messages between center and member on donation article's comment after
	 * designated message.
	 *
	 * @param donationArticleId Donation's id.
	 * @param memberId          Member's id.
	 * @param centerId          Center's id.
	 * @param messageId         Message's id.
	 * @return Chat messages between center and member on given donation comment(max 100).
	 */
	@Transactional(readOnly = true)
	public List<ChatMessageResponse> getDonationMessages(
		Long donationArticleId,
		Long memberId,
		Long centerId,
		Long messageId
	) {
		return chatMessageRepository
			.findFirst100ByDonationAndMemberAndCenterAndIdGreaterThan(
				findDonation(donationArticleId),
				findMember(memberId),
				findCenter(centerId),
				messageId
			)
			.stream()
			.map(ChatMessageResponse::new)
			.collect(Collectors.toList());
	}

	/**
	 * Get 100 chat messages between center and member on wish article's comment.
	 *
	 * @param donationWishArticleId DonationWish's id.
	 * @param memberId              Member's id.
	 * @param centerId              Center's id.
	 * @param messageId             Message id for cursor based pagination.
	 * @return Chat messages between center and member on given wish comment(max 100).
	 */
	@Transactional(readOnly = true)
	public List<ChatMessageResponse> getWishCommentMessages(
		Long donationWishArticleId,
		Long memberId,
		Long centerId,
		Long messageId
	) {
		return chatMessageRepository
			.findFirst100ByDonationWishAndMemberAndCenterAndIdGreaterThan(
				findDonationWish(donationWishArticleId),
				findMember(memberId),
				findCenter(centerId),
				messageId
			)
			.stream()
			.map(ChatMessageResponse::new)
			.collect(Collectors.toList());
	}

	/**
	 * Send chat on donation comment.
	 *
	 * @param donationArticleId Donation's id.
	 * @param memberId          Member's id.
	 * @param centerId          Center's id.
	 * @param senderType        UserType of sender for considering message direction.
	 * @param content           Chat message content.
	 * @return Sent chat message.
	 */
	public ChatMessageResponse sendDonationMessage(
		Long donationArticleId,
		Long memberId,
		Long centerId,
		UserType senderType,
		String content
	) {
		ChatMessage chatMessage = ChatMessage
			.builder()
			.content(content)
			.center(findCenter(centerId))
			.member(findMember(memberId))
			.donation(findDonation(donationArticleId))
			.senderType(senderType)
			.build();
		return new ChatMessageResponse(chatMessageRepository.save(chatMessage));
	}

	/**
	 * Send chat on donation wish comment.
	 *
	 * @param donationWishArticleId DonationWish's id.
	 * @param memberId              Member's id.
	 * @param centerId              Center's id.
	 * @param senderType            UserType of sender for considering message direction.
	 * @param content               Chat message content.
	 * @return Sent chat message.
	 */
	public ChatMessageResponse sendDonationWishMessage(
		Long donationWishArticleId,
		Long memberId,
		Long centerId,
		UserType senderType,
		String content
	) {
		ChatMessage chatMessage = ChatMessage
			.builder()
			.content(content)
			.center(findCenter(centerId))
			.member(findMember(memberId))
			.donationWish(findDonationWish(donationWishArticleId))
			.senderType(senderType)
			.build();
		return new ChatMessageResponse(chatMessageRepository.save(chatMessage));
	}

}
