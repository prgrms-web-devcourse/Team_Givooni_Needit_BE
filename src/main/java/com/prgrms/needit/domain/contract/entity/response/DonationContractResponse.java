package com.prgrms.needit.domain.contract.entity.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.prgrms.needit.domain.board.donation.entity.Donation;
import com.prgrms.needit.domain.contract.entity.Contract;
import lombok.Getter;

@Getter
public class DonationContractResponse extends ContractResponse {

	@JsonProperty("donationId")
	private final Long donationId;

	public DonationContractResponse(Contract contract, Donation donation) {
		super(contract);
		this.donationId = contract.getDonation() == null ?
			null :
			contract.getDonation()
					.getId();
	}

}
