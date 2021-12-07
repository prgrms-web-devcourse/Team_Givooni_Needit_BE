package com.prgrms.needit.domain.board.donation.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DonationFilterRequest {

	private String title;
	private String category;
	private List<Long> tags = new ArrayList<>();

}
