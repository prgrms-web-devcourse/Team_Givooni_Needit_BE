package com.prgrms.needit.domain.message.service;

import com.prgrms.needit.common.enums.BoardType;
import com.prgrms.needit.common.enums.UserType;
import com.prgrms.needit.common.error.ErrorCode;
import com.prgrms.needit.common.error.exception.InvalidArgumentException;
import com.prgrms.needit.common.error.exception.NotFoundResourceException;
import com.prgrms.needit.domain.board.donation.entity.Donation;
import com.prgrms.needit.domain.board.donation.repository.DonationRepository;
import com.prgrms.needit.domain.board.wish.entity.DonationWish;
import com.prgrms.needit.domain.board.wish.repository.DonationWishRepository;
import com.prgrms.needit.domain.message.entity.ChatMessage;
import com.prgrms.needit.domain.message.entity.response.ChatMessageResponse;
import com.prgrms.needit.domain.message.repository.ChatMessageRepository;
import com.prgrms.needit.domain.notification.service.NotificationService;
import com.prgrms.needit.domain.user.center.entity.Center;
import com.prgrms.needit.domain.user.center.repository.CenterRepository;
import com.prgrms.needit.domain.user.member.entity.Member;
import com.prgrms.needit.domain.user.member.repository.MemberRepository;
import com.prgrms.needit.domain.user.user.dto.CurUser;
import com.prgrms.needit.domain.user.user.service.UserService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
	private final UserService userService;
	private final NotificationService notificationService;

	private Donation findDonation(Long donationId) {
		return donationRepository
			.findById(donationId)
			.orElseThrow(() -> new NotFoundResourceException(ErrorCode.NOT_FOUND_DONATION));
	}

	private Member findMember(Long memberId) {
		return memberRepository
			.findById(memberId)
			.orElseThrow(() -> new NotFoundResourceException(ErrorCode.NOT_FOUND_MEMBER));
	}

	private Center findCenter(Long centerId) {
		return centerRepository
			.findById(centerId)
			.orElseThrow(() -> new NotFoundResourceException(ErrorCode.NOT_FOUND_CENTER));
	}

	private DonationWish findDonationWish(Long donationWishId) {
		return donationWishRepository
			.findById(donationWishId)
			.orElseThrow(() -> new NotFoundResourceException(ErrorCode.NOT_FOUND_DONATION_WISH));
	}

	/**
	 * Read chats of member grouped by donation, donation with article.
	 *
	 * @return Chats of member group by post.
	 */
	@Transactional(readOnly = true)
	public List<ChatMessageResponse> getCurrentUserChats() {
		CurUser curUser = userService.getCurUser();
		if (curUser.getRole()
				   .equals(UserType.MEMBER.name())) {
			return getMemberChats(curUser.getId());
		} else if (curUser.getRole()
						  .equals(UserType.CENTER.name())) {
			return getCenterChats(curUser.getId());
		}

		throw new InvalidArgumentException(ErrorCode.NOT_FOUND_USER);
	}

	@Transactional(readOnly = true)
	public List<ChatMessageResponse> getMemberChats(Long memberId) {
		return chatMessageRepository.findMessagesOfMemberAsGroup(findMember(memberId))
									.stream()
									.map(ChatMessageResponse::new)
									.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public List<ChatMessageResponse> getCenterChats(Long centerId) {
		return chatMessageRepository.findMessagesOfCenterAsGroup(findCenter(centerId))
									.stream()
									.map(ChatMessageResponse::new)
									.collect(Collectors.toList());
	}

	/**
	 * Get 100 chat messages between center and member on donation article's comment after
	 * designated message.
	 *
	 * @param articleId  Article's id.
	 * @param boardType  Article's type.
	 * @param receiverId Other chatting user's id.
	 * @param messageId  Message's id.
	 * @return Chat messages between center and member on given donation comment(max 100).
	 */
	@Transactional(readOnly = true)
	public List<ChatMessageResponse> getMessagesOnArticle(
		Long articleId,
		BoardType boardType,
		Long receiverId,
		Long messageId
	) {
		List<ChatMessage> messages = new ArrayList<>();
		Optional<Member> curMember = userService.getCurMember();
		Optional<Center> curCenter = userService.getCurCenter();

		if (curMember.isPresent()) {
			switch (boardType) {
				case DONATION:
					messages = chatMessageRepository
						.findFirst100ByDonationAndMemberAndCenterAndIdGreaterThan(
							findDonation(articleId),
							curMember.get(),
							findCenter(receiverId),
							messageId
						);
					break;

				case WISH:
					messages = chatMessageRepository
						.findFirst100ByDonationWishAndMemberAndCenterAndIdGreaterThan(
							findDonationWish(articleId),
							curMember.get(),
							findCenter(receiverId),
							messageId
						);
					break;

				default:
					throw new InvalidArgumentException(ErrorCode.INVALID_BOARD_TYPE);
			}
		} else if (curCenter.isPresent()) {
			switch (boardType) {
				case DONATION:
					messages = chatMessageRepository
						.findFirst100ByDonationAndMemberAndCenterAndIdGreaterThan(
							findDonation(articleId),
							findMember(receiverId),
							curCenter.get(),
							messageId
						);
					break;

				case WISH:
					messages = chatMessageRepository
						.findFirst100ByDonationWishAndMemberAndCenterAndIdGreaterThan(
							findDonationWish(articleId),
							findMember(receiverId),
							curCenter.get(),
							messageId
						);
					break;

				default:
					throw new InvalidArgumentException(ErrorCode.INVALID_BOARD_TYPE);
			}
		}

		return messages
			.stream()
			.map(ChatMessageResponse::new)
			.collect(Collectors.toList());
	}

	/**
	 * Send chat on donation comment.
	 *
	 * @param articleId  Article's id.
	 * @param boardType  Article's type.
	 * @param receiverId Chat receiver's id.
	 * @param content    Chat message content.
	 * @return Sent chat message.
	 */
	public ChatMessageResponse sendMessage(
		Long articleId,
		BoardType boardType,
		Long receiverId,
		String content
	) {
		Optional<Member> curMember = userService.getCurMember();
		Optional<Center> curCenter = userService.getCurCenter();

		ChatMessage.ChatMessageBuilder builder = ChatMessage
			.builder()
			.content(content);

		if (curMember.isPresent()) {
			builder.member(curMember.get());
			builder.center(findCenter(receiverId));
			builder.senderType(UserType.MEMBER);
		} else if (curCenter.isPresent()) {
			builder.member(findMember(receiverId));
			builder.center(curCenter.get());
			builder.senderType(UserType.CENTER);
		}

		switch (boardType) {
			case DONATION:
				builder.donation(findDonation(articleId));
				break;

			case WISH:
				builder.donationWish(findDonationWish(articleId));
				break;

			default:
				throw new InvalidArgumentException(ErrorCode.INVALID_BOARD_TYPE);
		}

		ChatMessage sentChat = chatMessageRepository.save(builder.build());
		if (sentChat.getSenderType()
					.equals(UserType.MEMBER)) {
			notificationService.sendChatNotification(
				sentChat.getMember()
						.getEmail(),
				new ChatMessageResponse(sentChat)
			);
		}

		if (sentChat.getSenderType()
					.equals(UserType.CENTER)) {
			notificationService.sendChatNotification(
				sentChat.getCenter()
						.getEmail(),
				new ChatMessageResponse(sentChat)
			);
		}

		return new ChatMessageResponse(sentChat);
	}

}
