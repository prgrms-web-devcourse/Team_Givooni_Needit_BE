package com.prgrms.needit.domain.contract.entity.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.prgrms.needit.domain.contract.entity.Contract;
import com.prgrms.needit.domain.contract.entity.enums.ContractStatus;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class ContractResponse {

	@JsonProperty("contractId")
	private final long id;

	@JsonProperty("contractDate")
	private final LocalDateTime contractDate;

	@JsonProperty("status")
	private final ContractStatus contractStatus;

	@JsonProperty("donationId")
	private final Long donationId;

	@JsonProperty("donationWishId")
	private final Long donationWishId;

	public ContractResponse(Contract contract) {
		this.id = contract.getId();
		this.contractDate = contract.getContractDate();
		this.contractStatus = contract.getStatus();
		this.donationId = contract.getDonation() == null ?
			null :
			contract.getDonation()
					.getId();
		this.donationWishId = contract.getDonationWish() == null ?
			null :
			contract.getDonationWish()
					.getId();
	}

}
