package com.prgrms.needit.domain.message.service;

import com.prgrms.needit.common.domain.enums.UserType;
import com.prgrms.needit.domain.board.donation.entity.DonationComment;
import com.prgrms.needit.domain.board.donation.repository.DonationRepository;
import com.prgrms.needit.domain.board.wish.entity.DonationWishComment;
import com.prgrms.needit.domain.board.wish.repository.DonationWishRepository;
import com.prgrms.needit.domain.message.controller.bind.MessageContractRequest;
import com.prgrms.needit.domain.message.entity.ContractStatus;
import com.prgrms.needit.domain.message.entity.MessageContract;
import com.prgrms.needit.domain.message.entity.MessageType;
import com.prgrms.needit.domain.message.entity.PostType;
import com.prgrms.needit.domain.message.entity.response.MessageContractResponse;
import com.prgrms.needit.domain.message.repository.MessageContractRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MessageContractService {

	private final MessageContractRepository messageContractRepository;
	private final DonationRepository donationRepository;
	private final DonationWishRepository donationWishRepository;

	@Transactional(readOnly = true)
	public List<MessageContractResponse> getCommentMessages(
		long articleId,
		long commentId,
		PostType postType,
		Pageable pageable
	) {
		return messageContractRepository.findAllByPostIdAndPostTypeAndCommentId(
			articleId, postType, commentId, pageable)
										.stream()
										.map(MessageContractResponse::new)
										.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public List<MessageContractResponse> readMessagesAfter(
		long articleId,
		long commentId,
		PostType postType,
		long messageIdCursor
	) {
		return messageContractRepository.findFirst100ByPostIdAndPostTypeAndCommentIdAndIdGreaterThanEqual(
			articleId, postType, commentId, messageIdCursor)
										.stream()
										.map(MessageContractResponse::new)
										.collect(Collectors.toList());
	}

	public MessageContractResponse sendDonationMessage(
		long donationArticleId,
		long commentId,
		long senderId,
		UserType senderType,
		MessageContractRequest request
	) {
		MessageType messageType = request.getMessageType();
		messageType.validate(request);
		// TODO: convert to donation's exception later.
		DonationComment donationComment = donationRepository.findById(donationArticleId)
															.orElseThrow(
																() -> new IllegalArgumentException(
																	"Donation with given id not found."))
															.getComments()
															.stream()
															.filter(comment -> comment.getId()
																== commentId)
															.findFirst()
															.orElseThrow(
																() -> new IllegalArgumentException(
																	"Donation comment with given id not found."));
		MessageContract.MessageContractBuilder messageContractBuilder = MessageContract
			.builder()
			.content(request.getContent())
			.messageType(messageType)
			.senderId(senderId)
			.senderType(senderType)
			.postId(donationArticleId)
			.commentId(commentId)
			.postType(PostType.DONATION)
			.status(messageType.equals(MessageType.CHAT) ? null : ContractStatus.REQUESTED)
			.date(messageType.equals(MessageType.CHAT) ? null : request.getContractDate());
		if (donationComment.getCenter()
						   .getId() == senderId) {
			messageContractBuilder.receiverId(donationComment.getDonation()
															 .getMember()
															 .getId());
			messageContractBuilder.receiverType(UserType.MEMBER);
		} else {
			messageContractBuilder.receiverId(donationComment.getCenter()
															 .getId());
			messageContractBuilder.receiverType(UserType.CENTER);
		}

		return new MessageContractResponse(
			messageContractRepository.save(messageContractBuilder.build()));
	}

	public MessageContractResponse sendDonationWishMessage(
		long wishArticleId,
		long commentId,
		long senderId,
		UserType senderType,
		MessageContractRequest request
	) {
		MessageType messageType = request.getMessageType();
		messageType.validate(request);
		// TODO: convert to donation's exception later.
		DonationWishComment wishComment = donationWishRepository.findById(wishArticleId)
																.orElseThrow(
																	() -> new IllegalArgumentException(
																		"Donation wish with given id not found."))
																.getComments()
																.stream()
																.filter(comment ->
																			comment.getId()
																				== commentId)
																.findFirst()
																.orElseThrow(
																	() -> new IllegalArgumentException(
																		"Donation wish comment with given id not found."));
		MessageContract.MessageContractBuilder messageContractBuilder = MessageContract
			.builder()
			.content(request.getContent())
			.messageType(messageType)
			.senderId(senderId)
			.senderType(senderType)
			.postId(wishArticleId)
			.commentId(commentId)
			.postType(PostType.DONATION_WISH)
			.status(messageType.equals(MessageType.CHAT) ? null : ContractStatus.REQUESTED)
			.date(messageType.equals(MessageType.CHAT) ? null : request.getContractDate());
		if (wishComment.getMember()
					   .getId() == senderId) {
			messageContractBuilder.receiverId(wishComment.getDonationWish()
														 .getCenter()
														 .getId());
			messageContractBuilder.receiverType(UserType.CENTER);
		} else {
			messageContractBuilder.receiverId(wishComment.getMember()
														 .getId());
			messageContractBuilder.receiverType(UserType.MEMBER);
		}

		return new MessageContractResponse(
			messageContractRepository.save(messageContractBuilder.build()));
	}

}
