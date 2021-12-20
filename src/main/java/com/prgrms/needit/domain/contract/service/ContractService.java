package com.prgrms.needit.domain.contract.service;

import com.prgrms.needit.common.enums.UserType;
import com.prgrms.needit.common.error.ErrorCode;
import com.prgrms.needit.common.error.exception.InvalidArgumentException;
import com.prgrms.needit.common.error.exception.NotFoundResourceException;
import com.prgrms.needit.domain.board.donation.entity.Donation;
import com.prgrms.needit.domain.board.donation.entity.DonationComment;
import com.prgrms.needit.domain.board.donation.repository.CommentRepository;
import com.prgrms.needit.domain.board.donation.repository.DonationRepository;
import com.prgrms.needit.domain.board.wish.entity.DonationWish;
import com.prgrms.needit.domain.board.wish.entity.DonationWishComment;
import com.prgrms.needit.domain.board.wish.repository.DonationWishRepository;
import com.prgrms.needit.domain.board.wish.repository.WishCommentRepository;
import com.prgrms.needit.domain.contract.entity.Contract;
import com.prgrms.needit.domain.contract.entity.enums.ContractStatus;
import com.prgrms.needit.domain.contract.entity.response.ContractResponse;
import com.prgrms.needit.domain.contract.exception.IllegalContractStatusException;
import com.prgrms.needit.domain.contract.repository.ContractRepository;
import com.prgrms.needit.domain.message.entity.ChatMessage;
import com.prgrms.needit.domain.message.entity.response.ChatMessageResponse;
import com.prgrms.needit.domain.notification.service.NotificationService;
import com.prgrms.needit.domain.user.center.entity.Center;
import com.prgrms.needit.domain.user.center.repository.CenterRepository;
import com.prgrms.needit.domain.user.member.entity.Member;
import com.prgrms.needit.domain.user.member.repository.MemberRepository;
import com.prgrms.needit.domain.user.user.service.UserService;
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

	private final DonationRepository donationRepository;
	private final DonationWishRepository donationWishRepository;
	private final ContractRepository contractRepository;
	private final CommentRepository donationCommentRepository;
	private final WishCommentRepository donationWishCommentRepository;
	private final UserService userService;
	private final MemberRepository memberRepository;
	private final CenterRepository centerRepository;
	private final NotificationService notificationService;

	private UserType getCurrentUserType() {
		return UserType.valueOf(userService.getCurUser()
										   .getRole());
	}

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
		Contract contract = getContract(contractId);
		String contractWith;
		switch (getCurrentUserType()) {
			case CENTER:
				contractWith = contract.getMember()
									   .getNickname();
				break;

			case MEMBER:
				contractWith = contract.getCenter()
									   .getName();
				break;

			default:
				throw new NotFoundResourceException(ErrorCode.NOT_FOUND_USER);
		}
		return new ContractResponse(contract, contractWith);
	}

	private Member findMember(Long memberId) {
		return memberRepository
			.findById(memberId)
			.orElseThrow(() -> new NotFoundResourceException(
				ErrorCode.NOT_FOUND_USER));
	}

	private Center findCenter(Long centerId) {
		return centerRepository
			.findById(centerId)
			.orElseThrow(() -> new NotFoundResourceException(
				ErrorCode.NOT_FOUND_USER));
	}

	private Donation findDonation(Long donationId) {
		return donationRepository
			.findById(donationId)
			.orElseThrow(() -> new NotFoundResourceException(
				ErrorCode.NOT_FOUND_DONATION));
	}

	private DonationWish findDonationWish(Long donationWishId) {
		return donationWishRepository
			.findById(donationWishId)
			.orElseThrow(() -> new NotFoundResourceException(
				ErrorCode.NOT_FOUND_DONATION_WISH));
	}

	@Transactional(readOnly = true)
	public List<ContractResponse> readMyContracts() {
		Optional<Member> curMember = userService.getCurMember();
		Optional<Center> curCenter = userService.getCurCenter();

		if (curMember.isPresent()) {
			return contractRepository
				.findAllByMember(curMember.get())
				.stream()
				.filter(Contract::isValid)
				.map(contract ->
						 new ContractResponse(
							 contract,
							 contract.getCenter()
									 .getName()
						 ))
				.collect(Collectors.toList());
		}

		if (curCenter.isPresent()) {
			return contractRepository
				.findAllByCenter(curCenter.get())
				.stream()
				.filter(Contract::isValid)
				.map(contract ->
						 new ContractResponse(
							 contract,
							 contract.getMember()
									 .getNickname()
						 ))
				.collect(Collectors.toList());
		}

		throw new NotFoundResourceException(ErrorCode.NOT_FOUND_USER);
	}

	/**
	 * Create contract.
	 *
	 * @param contractDate Date of contract.
	 * @param donationId   Donation's id.
	 * @param receiverId   Contract receiver's id.
	 * @return Created donation contract information.
	 */
	public ContractResponse createDonationContract(
		LocalDateTime contractDate,
		Long donationId,
		Long receiverId
	) {
		Donation donation = findDonation(donationId);
		DonationComment donationComment;
		String contractWith;
		UserType curUserType = getCurrentUserType();
		if (curUserType.equals(UserType.MEMBER)) {
			Center center = findCenter(receiverId);
			donationComment = donationCommentRepository
				.findByDonationAndCenter(donation, center)
				.orElseThrow(
					() -> new NotFoundResourceException(ErrorCode.NOT_FOUND_APPLY_COMMENT));

			if (!donationComment.getDonation()
								.getMember()
								.getId()
								.equals(userService.getCurUser()
												   .getId())) {
				throw new InvalidArgumentException(ErrorCode.NOT_MATCH_WRITER);
			}

			contractWith = center.getName();
		} else if (curUserType.equals(UserType.CENTER)) {
			Center center = userService
				.getCurCenter()
				.orElseThrow(() -> new NotFoundResourceException(
					ErrorCode.NOT_FOUND_CENTER));
			donationComment = donationCommentRepository
				.findByDonationAndCenter(donation, center)
				.orElseThrow(
					() -> new NotFoundResourceException(ErrorCode.NOT_MATCH_WRITER));
			Member member = donationComment.getDonation()
										   .getMember();

			if (!member.getId()
					   .equals(receiverId)) {
				throw new InvalidArgumentException(ErrorCode.NOT_MATCH_WRITER);
			}
			contractWith = member.getNickname();
		} else {
			throw new NotFoundResourceException(ErrorCode.NOT_FOUND_USER);
		}

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
			.senderType(getCurrentUserType())
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

		notificationService.sendChatNotification(
			contract.getCenter().getEmail(),
			new ChatMessageResponse(offerMessage));
		notificationService.sendChatNotification(
			contract.getMember().getEmail(),
			new ChatMessageResponse(offerMessage));

		return new ContractResponse(contractRepository.save(contract), contractWith);
	}

	/**
	 * Create donation wish('기부원해요') contract.
	 *
	 * @param contractDate   Date of contract.
	 * @param donationWishId Donation wish's id.
	 * @param receiverId     Contract receiver's id.
	 * @return Created donation wish contract information.
	 */
	public ContractResponse createDonationWishContract(
		LocalDateTime contractDate,
		Long donationWishId,
		Long receiverId
	) {
		DonationWish donationWish = findDonationWish(donationWishId);
		DonationWishComment wishComment;
		String contractWith;
		UserType curUserType = getCurrentUserType();
		if (curUserType.equals(UserType.MEMBER)) {
			Member member = userService
				.getCurMember()
				.orElseThrow(() -> new NotFoundResourceException(
					ErrorCode.NOT_FOUND_MEMBER));
			wishComment = donationWishCommentRepository
				.findByDonationWishAndMember(donationWish, member)
				.orElseThrow(
					() -> new NotFoundResourceException(ErrorCode.NOT_FOUND_WISH_COMMENT));
			Center center = wishComment.getDonationWish()
									   .getCenter();

			if (!center.getId()
					   .equals(receiverId)) {
				throw new InvalidArgumentException(ErrorCode.NOT_MATCH_WRITER);
			}
			contractWith = center.getName();
		} else if (curUserType.equals(UserType.CENTER)) {
			Member member = findMember(receiverId);
			wishComment = donationWishCommentRepository
				.findByDonationWishAndMember(donationWish, member)
				.orElseThrow(
					() -> new InvalidArgumentException(ErrorCode.NOT_MATCH_WRITER));

			if (!wishComment.getDonationWish()
							.getCenter()
							.getId()
							.equals(userService.getCurUser()
											   .getId())) {
				throw new InvalidArgumentException(ErrorCode.NOT_MATCH_WRITER);
			}

			contractWith = member.getNickname();
		} else {
			throw new NotFoundResourceException(ErrorCode.NOT_FOUND_USER);
		}

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
			.senderType(getCurrentUserType())
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

		notificationService.sendChatNotification(
			contract.getCenter().getEmail(),
			new ChatMessageResponse(offerMessage));
		notificationService.sendChatNotification(
			contract.getMember().getEmail(),
			new ChatMessageResponse(offerMessage));

		return new ContractResponse(contractRepository.save(contract), contractWith);
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
	public ContractResponse acceptContract(Long contractId) {
		Contract contract = findContract(contractId);
		if (getCurrentUserType().equals(contract.getChatMessage()
												.getSenderType())) {
			throw new IllegalContractStatusException(ErrorCode.INVALID_STATUS_CHANGE);
		}
		contract.acceptRequest();
		return readContract(contractId);
	}

	/**
	 * Refuse donation/donation-wish contract.
	 *
	 * @param contractId Contract's id.
	 * @return Refused contract's base information.
	 */
	public ContractResponse refuseContract(Long contractId) {
		Contract contract = findContract(contractId);
		if (getCurrentUserType().equals(contract.getChatMessage()
												.getSenderType())) {
			throw new IllegalContractStatusException(ErrorCode.INVALID_STATUS_CHANGE);
		}
		contract.refuseRequest();
		return readContract(contractId);
	}

}
