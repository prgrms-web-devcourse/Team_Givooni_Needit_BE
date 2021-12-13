package com.prgrms.needit.domain.contract.service;

import com.prgrms.needit.common.enums.UserType;
import com.prgrms.needit.common.error.ErrorCode;
import com.prgrms.needit.common.error.exception.NotFoundResourceException;
import com.prgrms.needit.domain.board.donation.entity.DonationComment;
import com.prgrms.needit.domain.board.donation.repository.CommentRepository;
import com.prgrms.needit.domain.board.wish.entity.DonationWishComment;
import com.prgrms.needit.domain.board.wish.repository.WishCommentRepository;
import com.prgrms.needit.domain.contract.entity.Contract;
import com.prgrms.needit.domain.contract.entity.enums.ContractStatus;
import com.prgrms.needit.domain.contract.entity.response.ContractResponse;
import com.prgrms.needit.domain.contract.repository.ContractRepository;
import com.prgrms.needit.domain.message.entity.ChatMessage;
import com.prgrms.needit.domain.user.center.entity.Center;
import com.prgrms.needit.domain.user.login.service.UserService;
import com.prgrms.needit.domain.user.member.entity.Member;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ContractService {

	private final ContractRepository contractRepository;
	private final CommentRepository donationCommentRepository;
	private final WishCommentRepository donationWishCommentRepository;
	private final UserService userService;

	private Contract getContract(Long contractId) {
		return contractRepository
			.findById(contractId)
			.orElseThrow(() -> new NotFoundResourceException(ErrorCode.NOT_FOUND_CONTRACT));
	}

	/**
	 * Read contract.
	 *
	 * @param contractId Contract's id.
	 * @return Contract information.
	 */
	@Transactional(readOnly = true)
	public ContractResponse readContract(Long contractId) {
		return new ContractResponse(getContract(contractId));
	}

	private DonationComment findDonationComment(Long donationCommentId) {
		return donationCommentRepository
			.findById(donationCommentId)
			.orElseThrow(() -> new NotFoundResourceException(
				ErrorCode.NOT_FOUND_APPLY_COMMENT));
	}

	@Transactional(readOnly = true)
	public List<ContractResponse> readMyContracts() {
		Optional<Member> curMember = userService.getCurMember();
		Optional<Center> curCenter = userService.getCurCenter();

		if(curMember.isPresent()) {
			return contractRepository.findAllByMember(curMember.get())
				.stream()
				.map(ContractResponse::new)
				.collect(Collectors.toList());
		}

		if(curCenter.isPresent()) {
			return contractRepository.findAllByCenter(curCenter.get())
				.stream()
				.map(ContractResponse::new)
				.collect(Collectors.toList());
		}

		throw new NotFoundResourceException(ErrorCode.NOT_FOUND_USER);
	}

	/**
	 * Create contract.
	 *
	 * @param contractDate      Date of contract.
	 * @param donationCommentId Donation comment's id.
	 * @param senderType        UserType of contract creator.
	 * @return Created donation contract information.
	 */
	public ContractResponse createDonationContract(
		LocalDateTime contractDate,
		long donationCommentId,
		UserType senderType
	) {
		DonationComment donationComment = findDonationComment(donationCommentId);
		Center center = donationComment.getCenter();
		Member member = donationComment.getDonation()
									   .getMember();
		ChatMessage offerMessage = ChatMessage
			.builder()
			.content(donationComment.getDonation()
									.getTitle())
			.center(center)
			.member(member)
			.donation(donationComment.getDonation())
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
		return new ContractResponse(contractRepository.save(contract));
	}

	private DonationWishComment findDonationWishComment(Long donationWishCommentId) {
		return donationWishCommentRepository
			.findById(donationWishCommentId)
			.orElseThrow(() -> new NotFoundResourceException(ErrorCode.NOT_FOUND_WISH_COMMENT));
	}

	/**
	 * Create donation wish('기부원해요') contract.
	 *
	 * @param contractDate          Date of contract.
	 * @param donationWishCommentId Donation wish comment's id.
	 * @param senderType            UserType of contract creator.
	 * @return Created donation wish contract information.
	 */
	public ContractResponse createDonationWishContract(
		LocalDateTime contractDate,
		Long donationWishCommentId,
		UserType senderType
	) {
		DonationWishComment wishComment = findDonationWishComment(donationWishCommentId);
		Center center = wishComment.getDonationWish()
								   .getCenter();
		Member member = wishComment.getMember();
		ChatMessage offerMessage = ChatMessage
			.builder()
			.content(wishComment.getDonationWish()
								.getTitle())
			.center(center)
			.member(member)
			.donationWish(wishComment.getDonationWish())
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
		return new ContractResponse(contractRepository.save(contract));
	}

	private Contract findContract(Long contractId) {
		return contractRepository
			.findById(contractId)
			.orElseThrow(() -> new NotFoundResourceException(ErrorCode.NOT_FOUND_CONTRACT));
	}

	/**
	 * Accept donation/donation-wish contract.
	 *
	 * @param contractId Contract's id.
	 * @return Accepted contract's base information.
	 */
	// TODO: authorize current user can accept/refuse this order or not.
	public ContractResponse acceptContract(Long contractId) {
		Contract contract = findContract(contractId);
		contract.acceptRequest();
		return new ContractResponse(contract);
	}

	/**
	 * Refuse donation/donation-wish contract.
	 *
	 * @param contractId Contract's id.
	 * @return Refused contract's base information.
	 */
	// TODO: authorize current user can accept/refuse this order or not.
	public ContractResponse refuseContract(Long contractId) {
		Contract contract = findContract(contractId);
		contract.refuseRequest();
		return new ContractResponse(contract);
	}

}
