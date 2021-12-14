package com.prgrms.needit.domain.board.donation.dto;

import com.prgrms.needit.domain.board.donation.entity.DonationComment;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class CommentResponse {

	private Long id;
	private String comment;
	private Long userId;
	private String userName;
	private String userImage;
	private LocalDateTime createdDate;
	private LocalDateTime updatedDate;

	public CommentResponse(
		DonationComment comment
	) {
		this.id = comment.getId();
		this.comment = comment.getComment();
		this.userId = comment.getCenter().getId();
		this.userName = comment.getCenter().getName();
		this.userImage = comment.getCenter().getProfileImageUrl();
		this.createdDate = comment.getCreatedAt();
		this.updatedDate = comment.getUpdatedAt();
	}
}
