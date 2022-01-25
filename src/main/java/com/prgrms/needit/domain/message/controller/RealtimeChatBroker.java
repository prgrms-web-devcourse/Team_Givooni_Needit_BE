package com.prgrms.needit.domain.message.controller;

import com.prgrms.needit.common.enums.UserType;
import com.prgrms.needit.common.error.ErrorCode;
import com.prgrms.needit.common.error.exception.InvalidArgumentException;
import com.prgrms.needit.common.error.exception.NotFoundResourceException;
import com.prgrms.needit.domain.board.donation.entity.Donation;
import com.prgrms.needit.domain.board.donation.entity.DonationComment;
import com.prgrms.needit.domain.board.donation.repository.DonationRepository;
import com.prgrms.needit.domain.board.wish.entity.DonationWish;
import com.prgrms.needit.domain.board.wish.entity.DonationWishComment;
import com.prgrms.needit.domain.board.wish.repository.DonationWishRepository;
import com.prgrms.needit.domain.center.entity.Center;
import com.prgrms.needit.domain.member.entity.Member;
import com.prgrms.needit.domain.message.controller.bind.ChatMessageRequest;
import com.prgrms.needit.domain.message.entity.ChatMessage;
import com.prgrms.needit.domain.message.entity.response.ChatMessageResponse;
import com.prgrms.needit.domain.message.repository.ChatMessageRepository;
import com.prgrms.needit.domain.notification.service.NotificationService;
import java.security.Principal;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Controller
@Transactional
@RequiredArgsConstructor
public class RealtimeChatBroker {

	private final NotificationService notificationService;
	private final DonationRepository donationRepository;
	private final DonationWishRepository donationWishRepository;
	private final ChatMessageRepository chatMessageRepository;

	@MessageMapping("/chat")
	@SendToUser("/topic/chats")
	public ChatMessageResponse sendChatOnArticle(
		@Payload ChatMessageRequest request,
		Principal principal
	) {
		if (principal == null) {
			log.info("Current principal: is null");
			throw new IllegalArgumentException("Current principal is null.");
		}

		log.info("Current principal: {}", principal.getName());
		String currentUsername = principal.getName();
		String receiverEmail;
		ChatMessage.ChatMessageBuilder chatMessageBuilder = ChatMessage
			.builder()
			.content(request.getContent());
		switch (request.getPostType()) {
			case DONATION:
				Donation donation = donationRepository
					.findById(request.getPostId())
					.orElseThrow(() -> new NotFoundResourceException(ErrorCode.NOT_FOUND_DONATION));
				Stream<Center> donationCommentedCenterStream = donation
					.getComments()
					.stream()
					.map(DonationComment::getCenter);
				chatMessageBuilder
					.donation(donation)
					.member(donation.getMember());

				if (donation.getMember()
							.getEmail()
							.equals(currentUsername)) {
					chatMessageBuilder.senderType(UserType.MEMBER);

					Center center = donationCommentedCenterStream
						.filter(c -> c.getId()
									  .equals(request.getReceiverId()))
						.findFirst()
						.orElseThrow(
							() -> new InvalidArgumentException(
								ErrorCode.UNAUTHORIZED_CHAT_DIRECTION));
					chatMessageBuilder.center(center);
					receiverEmail = center.getEmail();
				} else {
					Center center = donationCommentedCenterStream
						.filter(c -> c.getEmail()
									  .equals(currentUsername))
						.findAny()
						.orElseThrow(
							() -> new NotFoundResourceException(ErrorCode.NOT_FOUND_CENTER));
					chatMessageBuilder
						.center(center)
						.senderType(UserType.CENTER);

					receiverEmail = donation
						.getMember()
						.getEmail();
				}
				break;

			case WISH:
				DonationWish donationWish = donationWishRepository
					.findById(request.getPostId())
					.orElseThrow(
						() -> new NotFoundResourceException(ErrorCode.NOT_FOUND_DONATION_WISH));
				Stream<Member> donationWishCommentedMemberStream = donationWish
					.getComments()
					.stream()
					.map(DonationWishComment::getMember);
				chatMessageBuilder
					.donationWish(donationWish)
					.center(donationWish.getCenter());

				if (donationWish.getCenter()
								.getEmail()
								.equals(currentUsername)) {
					Member member = donationWishCommentedMemberStream
						.filter(m -> m.getId()
									  .equals(request.getReceiverId()))
						.findFirst()
						.orElseThrow(() -> new NotFoundResourceException(
							ErrorCode.UNAUTHORIZED_CHAT_DIRECTION));
					chatMessageBuilder
						.member(member)
						.senderType(UserType.CENTER);
					receiverEmail = member.getEmail();
				} else {
					Member member = donationWishCommentedMemberStream
						.filter(m -> m.getEmail()
									  .equals(currentUsername))
						.findFirst()
						.orElseThrow(
							() -> new NotFoundResourceException(ErrorCode.NOT_FOUND_MEMBER));
					chatMessageBuilder
						.member(member)
						.senderType(UserType.MEMBER);
					receiverEmail = donationWish
						.getCenter()
						.getEmail();
				}
				break;

			default:
				throw new InvalidArgumentException(ErrorCode.INVALID_BOARD_TYPE);
		}

		ChatMessageResponse sentChat = new ChatMessageResponse(
			chatMessageRepository.save(chatMessageBuilder.build()));
		notificationService.sendChatNotification(
			receiverEmail,
			sentChat
		);
		return sentChat;
	}

}
