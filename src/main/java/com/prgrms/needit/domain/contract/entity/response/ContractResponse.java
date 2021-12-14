package com.prgrms.needit.domain.contract.entity.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.prgrms.needit.domain.board.donation.dto.DonationResponse;
import com.prgrms.needit.domain.board.wish.dto.DonationWishResponse;
import com.prgrms.needit.domain.contract.entity.Contract;
import com.prgrms.needit.domain.contract.entity.enums.ContractStatus;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class ContractResponse {

	@JsonProperty("contractId")
	private final Long id;

	@JsonProperty("contractDate")
	private final LocalDateTime contractDate;

	@JsonProperty("status")
	private final ContractStatus contractStatus;

	@JsonProperty("donation")
	private final DonationResponse donation;

	@JsonProperty("donationWish")
	private final DonationWishResponse donationWish;

	public ContractResponse(Contract contract) {
		this.id = contract.getId();
		this.contractDate = contract.getContractDate();
		this.contractStatus = contract.getStatus();
		this.donation = contract.getDonation() == null ?
			null :
			new DonationResponse(contract.getDonation());
		this.donationWish = contract.getDonationWish() == null ?
			null :
			new DonationWishResponse(contract.getDonationWish());
	}

}
