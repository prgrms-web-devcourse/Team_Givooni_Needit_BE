package com.prgrms.needit.common.domain.dto;

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
	private String centerName;
	private String location;
	private List<Long> tags = new ArrayList<>();

}