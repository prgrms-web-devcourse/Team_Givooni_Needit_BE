package com.prgrms.needit.domain.board.donation.dto;

import com.prgrms.needit.domain.board.donation.entity.Donation;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DonationResponse {

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
	private List<CommentResponse> comments = new ArrayList<>();

	public DonationResponse(
		Donation donation
	) {
		this.id = donation.getId();
		this.title = donation.getTitle();
		this.content = donation.getContent();
		this.category = donation.getCategory().getType();
		this.quality = donation.getQuality().getType();
		this.status = donation.getStatus().getType();
		this.memberId = donation.getMember().getId();
		this.member = donation.getMember().getNickname();
		this.memberImage = donation.getMember().getProfileImageUrl();
		this.createdDate = donation.getCreatedAt();
		this.updatedDate = donation.getUpdatedAt();
		this.centerCnt = donation.getComments().size();
		this.tags = donation.getTags()
							.stream()
							.map(donationHaveTag -> donationHaveTag.getThemeTag().getTagName())
							.collect(Collectors.toList());
		this.comments = donation.getComments()
								.stream()
								.map(CommentResponse::new)
								.collect(Collectors.toList());
	}
}
