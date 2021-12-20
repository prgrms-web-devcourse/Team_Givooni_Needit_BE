package com.prgrms.needit.domain.board.donation.dto;

import com.prgrms.needit.common.domain.dto.CommentResponse;
import com.prgrms.needit.common.enums.BoardType;
import com.prgrms.needit.common.enums.UserType;
import com.prgrms.needit.domain.board.donation.entity.Donation;
import com.prgrms.needit.domain.board.donation.entity.DonationImage;
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
	private Long userId;
	private String userName;
	private String userEmail;
	private String userImage;
	private String userRole;
	private int userCnt;
	private String boardType;
	private LocalDateTime createdDate;
	private LocalDateTime updatedDate;
	private List<String> tags = new ArrayList<>();
	private List<String> images = new ArrayList<>();
	private List<CommentResponse> comments = new ArrayList<>();

	public DonationResponse(Donation donation) {
		this.id = donation.getId();
		this.title = donation.getTitle();
		this.content = donation.getContent();
		this.category = donation.getCategory()
								.getType();
		this.quality = donation.getQuality()
							   .getType();
		this.status = donation.getStatus()
							  .getType();
		this.userId = donation.getMember()
							  .getId();
		this.userName = donation.getMember()
								.getNickname();
		this.userEmail = donation.getMember()
								 .getEmail();
		this.userImage = donation.getMember()
								 .getProfileImageUrl();
		this.userRole = UserType.MEMBER.name();
		this.createdDate = donation.getCreatedAt();
		this.updatedDate = donation.getUpdatedAt();
		this.boardType = BoardType.DONATION.name();
		this.tags = donation.getTags()
							.stream()
							.map(donationTag -> donationTag.getThemeTag()
														   .getTagName())
							.collect(Collectors.toList());
		this.images = donation.getImages()
							  .stream()
							  .map(DonationImage::getUrl)
							  .collect(Collectors.toList());
		this.comments = donation.getComments()
								.stream()
								.map(CommentResponse::toResponse)
								.collect(Collectors.toList());
		this.userCnt = this.comments.size();
	}
}
