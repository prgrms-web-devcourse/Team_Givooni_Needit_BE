package com.prgrms.needit.domain.message.service;

import com.prgrms.needit.common.domain.enums.UserType;
import com.prgrms.needit.domain.board.donation.entity.DonationComment;
import com.prgrms.needit.domain.board.donation.repository.DonationCommentRepository;
import com.prgrms.needit.domain.board.wish.entity.DonationWishComment;
import com.prgrms.needit.domain.board.wish.repository.DonationWishCommentRepository;
import com.prgrms.needit.domain.contract.entity.Contract;
import com.prgrms.needit.domain.contract.repository.ContractRepository;
import com.prgrms.needit.domain.message.controller.bind.ChatMessageRequest;
import com.prgrms.needit.domain.message.entity.ChatMessage;
import com.prgrms.needit.domain.message.entity.response.ChatMessageResponse;
import com.prgrms.needit.domain.message.repository.ChatMessageRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.prgrms.needit.common.utils.EntityFinder.*;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatMessageService {

	private final ChatMessageRepository chatMessageRepository;
	private final DonationCommentRepository donationCommentRepository;
	private final DonationWishCommentRepository donationWishCommentRepository;
	private final ContractRepository contractRepository;

	/**
	 * Get 100 chat messages between center and member on donation article's comment.
	 *
	 * @param donationArticleId Donation's id.
	 * @param donationCommentId DonationComment's id.
	 * @param messageId         Message id for cursor based pagination.
	 * @return Chat messages between center and member on given donation comment(max 100).
	 */
	@Transactional(readOnly = true)
	public List<ChatMessageResponse> getDonationCommentMessages(
		long donationArticleId,
		long donationCommentId,
		long messageId
	) {
		DonationComment donationComment = findDonationComment(
			donationCommentRepository, donationArticleId, donationCommentId);
		return chatMessageRepository
			.findFirst100ByDonationCommentAndIdGreaterThan(donationComment, messageId)
			.stream()
			.map(ChatMessageResponse::new)
			.collect(Collectors.toList());
	}

	/**
	 * Get 100 chat messages between center and member on wish article's comment.
	 *
	 * @param donationWishArticleId DonationWish's id.
	 * @param donationWishCommentId DonationWishComment's id.
	 * @param messageId             Message id for cursor based pagination.
	 * @return Chat messages between center and member on given wish comment(max 100).
	 */
	@Transactional(readOnly = true)
	public List<ChatMessageResponse> getWishCommentMessages(
		long donationWishArticleId,
		long donationWishCommentId,
		long messageId
	) {
		DonationWishComment donationWishComment = findDonationWishComment(
			donationWishCommentRepository, donationWishArticleId, donationWishCommentId);
		return chatMessageRepository
			.findFirst100ByDonationWishCommentAndIdGreaterThan(donationWishComment, messageId)
			.stream()
			.map(ChatMessageResponse::new)
			.collect(Collectors.toList());
	}

	private Contract findContract(Long contractId) {
		if (contractId == null) {
			return null;
		}
		return contractRepository
			.findById(contractId)
			.orElseThrow(() -> new IllegalArgumentException("Contract with given id not found."));
	}

	/**
	 * Send chat on donation comment.
	 *
	 * @param donationArticleId Donation's id.
	 * @param commentId         DonationComment's id.
	 * @param senderType        UserType of sender for considering message direction.
	 * @param request           Chat message request.
	 * @return Sent chat message.
	 */
	public ChatMessageResponse sendDonationMessage(
		long donationArticleId,
		long commentId,
		UserType senderType,
		ChatMessageRequest request
	) {
		DonationComment donationComment = findDonationComment(
			donationCommentRepository, donationArticleId, commentId);
		ChatMessage chatMessage = ChatMessage
			.builder()
			.content(request.getContent())
			.center(donationComment.getCenter())
			.member(donationComment.getDonation()
								   .getMember())
			.donationComment(donationComment)
			.contract(findContract(request.getContractId()))
			.senderType(senderType)
			.build();
		return new ChatMessageResponse(chatMessageRepository.save(chatMessage));
	}

	/**
	 * Send chat on donation wish comment.
	 *
	 * @param donationWishArticleId DonationWish's id.
	 * @param commentId             DonationWishComment's id.
	 * @param senderType            UserType of sender for considering message direction.
	 * @param request               Chat message request.
	 * @return Sent chat message.
	 */
	public ChatMessageResponse sendDonationWishMessage(
		long donationWishArticleId,
		long commentId,
		UserType senderType,
		ChatMessageRequest request
	) {
		DonationWishComment wishComment = findDonationWishComment(
			donationWishCommentRepository, donationWishArticleId, commentId);
		ChatMessage chatMessage = ChatMessage
			.builder()
			.content(request.getContent())
			.center(wishComment.getDonationWish()
							   .getCenter())
			.member(wishComment.getMember())
			.donationWishComment(wishComment)
			.contract(findContract(request.getContractId()))
			.senderType(senderType)
			.build();
		return new ChatMessageResponse(chatMessageRepository.save(chatMessage));
	}

}
