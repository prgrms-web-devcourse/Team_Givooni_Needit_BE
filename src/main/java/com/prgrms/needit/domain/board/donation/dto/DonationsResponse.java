package com.prgrms.needit.domain.board.donation.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DonationsResponse {

	private Long id;
	private String title;
	private String content;
	private String category;
	private String quality;
	private String status;
	private Long memberId;
	private String member;
	private String memberImage;
	private int centerCnt;
	private LocalDateTime createdDate;
	private LocalDateTime updatedDate;
	private List<String> tags = new ArrayList<>();

}
