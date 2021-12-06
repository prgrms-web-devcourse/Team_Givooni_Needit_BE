package com.prgrms.needit.domain.contract.service;

import static com.prgrms.needit.common.utils.EntityFinder.*;

import com.prgrms.needit.common.domain.enums.UserType;
import com.prgrms.needit.domain.board.donation.entity.Donation;
import com.prgrms.needit.domain.board.donation.entity.DonationComment;
import com.prgrms.needit.domain.board.donation.repository.DonationCommentRepository;
import com.prgrms.needit.domain.board.donation.repository.DonationRepository;
import com.prgrms.needit.domain.board.wish.entity.DonationWish;
import com.prgrms.needit.domain.board.wish.entity.DonationWishComment;
import com.prgrms.needit.domain.board.wish.repository.DonationWishCommentRepository;
import com.prgrms.needit.domain.board.wish.repository.DonationWishRepository;
import com.prgrms.needit.domain.center.entity.Center;
import com.prgrms.needit.domain.contract.entity.Contract;
import com.prgrms.needit.domain.contract.entity.enums.ContractStatus;
import com.prgrms.needit.domain.contract.entity.response.ContractResponse;
import com.prgrms.needit.domain.contract.entity.response.DonationContractResponse;
import com.prgrms.needit.domain.contract.entity.response.WishContractResponse;
import com.prgrms.needit.domain.contract.exception.ContractNotFoundException;
import com.prgrms.needit.domain.contract.repository.ContractRepository;
import com.prgrms.needit.domain.member.entity.Member;
import com.prgrms.needit.domain.message.entity.ChatMessage;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ContractService {

	private final ContractRepository contractRepository;
	private final DonationRepository donationRepository;
	private final DonationWishRepository donationWishRepository;
	private final DonationCommentRepository donationCommentRepository;
	private final DonationWishCommentRepository donationWishCommentRepository;

	private Contract getDonationContract(Donation donation, long contractId) {
		return contractRepository
			.findByIdAndDonation(contractId, donation)
			.orElseThrow(() -> new ContractNotFoundException(contractId));
	}

	private Contract getDonationWishContract(DonationWish donationWish, long contractId) {
		return contractRepository
			.findByIdAndDonationWish(contractId, donationWish)
			.orElseThrow(() -> new ContractNotFoundException(contractId));
	}

	/**
	 * Read donation('기부할래요') contract.
	 * @param donationId Donation's id.
	 * @param contractId Contract's id.
	 * @return Donation contract information.
	 */
	@Transactional(readOnly = true)
	public DonationContractResponse readDonationContract(long donationId, long contractId) {
		Donation donation = donationRepository
			.findById(donationId)
			.orElseThrow(IllegalArgumentException::new); // TODO: change to donation not found exception.
		return new DonationContractResponse(getDonationContract(donation, contractId), donation);
	}

	/**
	 * Read donation wish('기부원해요') contract.
	 * @param donationWishId Donation wish's id.
	 * @param contractId Contract's id.
	 * @return Donation wish contract information.
	 */
	@Transactional(readOnly = true)
	public WishContractResponse readDonationWishContract(long donationWishId, long contractId) {
		DonationWish donationWish = donationWishRepository
			.findById(donationWishId)
			.orElseThrow(IllegalArgumentException::new); // TODO: change to donation not found exception.
		return new WishContractResponse(getDonationWishContract(donationWish, contractId), donationWish);
	}

	/**
	 * Create donation('기부할래요') contract.
 	 * @param contractDate Date of contract.
	 * @param donationArticleId Donation's id.
	 * @param donationCommentId Donation comment's id.
	 * @param senderType UserType of contract creator.
	 * @return Created donation contract information.
	 */
	public DonationContractResponse createDonationContract(
		LocalDateTime contractDate,
		long donationArticleId,
		long donationCommentId,
		UserType senderType
	) {
		DonationComment donationComment = findDonationComment(
			donationCommentRepository, donationArticleId, donationCommentId);
		Center center = donationComment.getCenter();
		Member member = donationComment.getDonation()
									   .getMember();
		ChatMessage offerMessage = ChatMessage
			.builder()
			.content(donationComment.getDonation()
									.getTitle())
			.center(center)
			.member(member)
			.donationComment(donationComment)
			.senderType(senderType)
			.build();

		Contract contract = Contract
			.builder()
			.contractDate(contractDate)
			.donation(donationComment.getDonation())
			.center(center)
			.member(member)
			.chatMessage(offerMessage)
			.status(ContractStatus.REQUESTED)
			.build();
		return new DonationContractResponse(contractRepository.save(contract), donationComment.getDonation());
	}

	/**
	 * Create donation wish('기부원해요') contract.
	 * @param contractDate Date of contract.
	 * @param donationWishArticleId Donation wish's id.
	 * @param donationWishCommentId Donation wish comment's id.
	 * @param senderType UserType of contract creator.
	 * @return Created donation wish contract information.
	 */
	public WishContractResponse createDonationWishContract(
		LocalDateTime contractDate,
		long donationWishArticleId,
		long donationWishCommentId,
		UserType senderType
	) {
		DonationWishComment wishComment = findDonationWishComment(
			donationWishCommentRepository, donationWishArticleId, donationWishCommentId);
		Center center = wishComment.getDonationWish()
								   .getCenter();
		Member member = wishComment.getMember();
		ChatMessage offerMessage = ChatMessage
			.builder()
			.content(wishComment.getDonationWish()
								.getTitle())
			.center(center)
			.member(member)
			.donationWishComment(wishComment)
			.senderType(senderType)
			.build();

		Contract contract = Contract
			.builder()
			.contractDate(contractDate)
			.donationWish(wishComment.getDonationWish())
			.center(center)
			.member(member)
			.chatMessage(offerMessage)
			.status(ContractStatus.REQUESTED)
			.build();
		return new WishContractResponse(contractRepository.save(contract), wishComment.getDonationWish());
	}

	private Contract findContract(long contractId) {
		return contractRepository
			.findById(contractId)
			.orElseThrow(() -> new ContractNotFoundException(contractId));
	}

	/**
	 * Accept donation/donation-wish contract.
	 * @param contractId Contract's id.
	 * @return Accepted contract's base information.
	 */
	// TODO: authorize to decide that user can accept/refuse this order or not.
	// requesting user cannot accept/refuse. only recipient.
	public ContractResponse acceptContract(long contractId) {
		Contract contract = findContract(contractId);
		contract.acceptRequest();
		return new ContractResponse(contract);
	}

	/**
	 * Refuse donation/donation-wish contract.
	 * @param contractId Contract's id.
	 * @return Refused contract's base information.
	 */
	public ContractResponse refuseContract(long contractId) {
		Contract contract = findContract(contractId);
		contract.refuseRequest();
		return new ContractResponse(contract);
	}

}
