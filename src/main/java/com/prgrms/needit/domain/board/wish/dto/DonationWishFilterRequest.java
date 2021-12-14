package com.prgrms.needit.domain.board.wish.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DonationWishFilterRequest {

	private String title;
	private String category;
	private String centerName;
	private String location;
	private List<Long> tags = new ArrayList<>();

}