package com.prgrms.needit.domain.board.donation.dto;

import com.prgrms.needit.domain.board.donation.entity.DonationComment;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class CommentResponse {

	private Long id;
	private String comment;
	private Long centerId;
	private String center;
	private String centerImage;
	private LocalDateTime createdDate;
	private LocalDateTime updatedDate;

	public CommentResponse(
		DonationComment comment
	) {
		this.id = comment.getId();
		this.comment = comment.getComment();
		this.centerId = comment.getCenter().getId();
		this.center = comment.getCenter().getName();
		this.centerImage = comment.getCenter().getProfileImageUrl();
		this.createdDate = comment.getCreatedAt();
		this.updatedDate = comment.getUpdatedAt();
	}
}
