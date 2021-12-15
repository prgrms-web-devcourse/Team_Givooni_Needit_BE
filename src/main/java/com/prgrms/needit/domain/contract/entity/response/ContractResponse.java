package com.prgrms.needit.domain.contract.entity.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.prgrms.needit.common.enums.BoardType;
import com.prgrms.needit.common.enums.DonationStatus;
import com.prgrms.needit.domain.board.donation.entity.Donation;
import com.prgrms.needit.domain.board.wish.entity.DonationWish;
import com.prgrms.needit.domain.contract.entity.Contract;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class ContractResponse {

	@JsonProperty("contractId")
	private final Long id;

	@JsonProperty("contractDate")
	private final LocalDateTime contractDate;

	@JsonProperty("status")
	private final DonationStatus donationStatus;

	@JsonProperty("postId")
	private final Long postId;

	@JsonProperty("postType")
	private final BoardType postType;

	@JsonProperty("postTitle")
	private final String postTitle;

	@JsonProperty("postContent")
	private final String postContent;

	@JsonProperty("contractWith")
	private final String contractWith;

	public ContractResponse(Contract contract) {
		this(contract, "");
	}

	public ContractResponse(Contract contract, String contractWith) {
		this.id = contract.getId();
		this.contractDate = contract.getContractDate();
		Donation donation = contract.getDonation();
		DonationWish donationWish = contract.getDonationWish();
		if(donation != null && donationWish == null) {
			this.donationStatus = donation.getStatus();
			this.postId = donation.getId();
			this.postType = BoardType.DONATION;
			this.postTitle = donation.getTitle();
			this.postContent = donation.getContent();
			this.contractWith = contractWith;
			return;
		}

		if(donationWish != null && donation == null) {
			this.donationStatus = donationWish.getStatus();
			this.postId = donationWish.getId();
			this.postType = BoardType.WISH;
			this.postTitle = donationWish.getTitle();
			this.postContent = donationWish.getContent();
			this.contractWith = contractWith;
			return;
		}

		// error but silently.
		this.donationStatus = null;
		this.postId = -1L;
		this.postType = null;
		this.postTitle = "";
		this.postContent = "";
		this.contractWith = "";
	}

}
