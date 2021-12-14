package com.prgrms.needit.domain.message.controller;

import com.prgrms.needit.common.error.ErrorCode;
import com.prgrms.needit.common.error.exception.InvalidArgumentException;
import com.prgrms.needit.common.error.exception.NotFoundResourceException;
import com.prgrms.needit.domain.board.donation.entity.Donation;
import com.prgrms.needit.domain.board.donation.entity.DonationComment;
import com.prgrms.needit.domain.board.donation.repository.DonationRepository;
import com.prgrms.needit.domain.board.wish.entity.DonationWish;
import com.prgrms.needit.domain.board.wish.entity.DonationWishComment;
import com.prgrms.needit.domain.board.wish.repository.DonationWishRepository;
import com.prgrms.needit.domain.message.controller.bind.ChatMessageRequest;
import com.prgrms.needit.domain.message.entity.response.ChatMessageResponse;
import com.prgrms.needit.domain.message.service.ChatMessageService;
import com.prgrms.needit.domain.notification.service.NotificationService;
import com.prgrms.needit.domain.user.center.entity.Center;
import com.prgrms.needit.domain.user.member.entity.Member;
import java.security.Principal;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class RealtimeChatBroker {

	private final DonationRepository donationRepository;
	private final DonationWishRepository donationWishRepository;
	private final ChatMessageService chatMessageService;
	private final NotificationService notificationService;

	@MessageMapping("/chat")
	public ChatMessageResponse sendChatOnArticle(
		@Payload ChatMessageRequest request,
		Principal principal
	) {
		String currentUsername = principal.getName();
		String receiverEmail;
		switch (request.getPostType()) {
			case DONATION: // '기부할래요' 게시글에 달린 댓글에서 채팅
				Donation donation = donationRepository
					.findById(request.getPostId())
					.orElseThrow(() -> new NotFoundResourceException(ErrorCode.NOT_FOUND_DONATION));
				Stream<Center> donationCommentedCenterStream = donation
					.getComments()
					.stream()
					.map(DonationComment::getCenter);

				// if chat sender is donation writer(member)
				if (donation.getMember()
							.getEmail()
							.equals(currentUsername)) {
					// get receiver email.
					receiverEmail = donationCommentedCenterStream
						.filter(center -> center.getId()
												.equals(request.getReceiverId()))
						.map(Center::getEmail)
						.findFirst()
						.orElseThrow(
							() -> new InvalidArgumentException(
								ErrorCode.UNAUTHORIZED_CHAT_DIRECTION));
				}
				// if chat sender is center who commented donation
				else if (donationCommentedCenterStream
					.anyMatch(center -> center.getEmail()
											  .equals(currentUsername))) {
					// get receiver email.
					receiverEmail = donation
						.getMember()
						.getEmail();
				} else {
					throw new InvalidArgumentException(ErrorCode.UNAUTHORIZED_CHAT_DIRECTION);
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

				// if sender is donation wish writer(center).
				if (donationWish.getCenter()
								.getEmail()
								.equals(currentUsername)) {
					// get receiver email.
					receiverEmail = donationWishCommentedMemberStream
						.filter(member -> member.getId()
												.equals(request.getReceiverId()))
						.findFirst()
						.orElseThrow(() -> new NotFoundResourceException(
							ErrorCode.UNAUTHORIZED_CHAT_DIRECTION))
						.getEmail();
				}
				// if sender is donation wish commenter(member).
				else if (donationWishCommentedMemberStream
					.anyMatch(member -> member.getEmail()
											  .equals(currentUsername))) {
					// get receiver email.
					receiverEmail = donationWish
						.getCenter()
						.getEmail();
				} else {
					throw new InvalidArgumentException(ErrorCode.UNAUTHORIZED_CHAT_DIRECTION);
				}
				break;

			default:
				throw new InvalidArgumentException(ErrorCode.INVALID_BOARD_TYPE);
		}

		ChatMessageResponse sentChat = chatMessageService.sendMessage(
			request.getPostId(),
			request.getPostType(),
			request.getReceiverId(),
			request.getContent()
		);
		// send new chatting message notification to receiver.
		notificationService.sendChatNotification(
			receiverEmail,
			sentChat
		);
		return sentChat;
	}

}
