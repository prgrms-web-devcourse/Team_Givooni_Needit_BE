package com.prgrms.needit.domain.board.donation.dto;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DonationStatusRequest {

	@NotBlank
	private String status;

	public DonationStatusRequest(String status) {
		this.status = status;
	}
}
