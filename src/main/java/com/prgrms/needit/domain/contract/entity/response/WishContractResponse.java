package com.prgrms.needit.domain.contract.entity.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.prgrms.needit.domain.board.wish.entity.DonationWish;
import com.prgrms.needit.domain.contract.entity.Contract;
import lombok.Getter;

@Getter
public class WishContractResponse extends ContractResponse {

	@JsonProperty("donationWishId")
	private final Long donationWishId;

	public WishContractResponse(Contract contract, DonationWish donationWish) {
		super(contract);
		this.donationWishId = contract.getDonationWish() == null ?
			null :
			contract.getDonationWish()
					.getId();
	}

}
