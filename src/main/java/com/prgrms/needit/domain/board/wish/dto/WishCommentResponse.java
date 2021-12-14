package com.prgrms.needit.domain.board.wish.dto;

import com.prgrms.needit.domain.board.wish.entity.DonationWishComment;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class WishCommentResponse {

	private Long id;
	private String comment;
	private Long userId;
	private String userName;
	private String userImage;
	private LocalDateTime createdDate;
	private LocalDateTime updatedDate;

	public WishCommentResponse(
		DonationWishComment comment
	) {
		this.id = comment.getId();
		this.comment = comment.getComment();
		this.userId = comment.getMember().getId();
		this.userName = comment.getMember().getNickname();
		this.userImage = comment.getMember().getProfileImageUrl();
		this.createdDate = comment.getCreatedAt();
		this.updatedDate = comment.getUpdatedAt();
	}
}
