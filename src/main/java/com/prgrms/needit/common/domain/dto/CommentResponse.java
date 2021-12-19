package com.prgrms.needit.common.domain.dto;

import com.prgrms.needit.common.enums.UserType;
import com.prgrms.needit.domain.board.activity.entity.ActivityComment;
import com.prgrms.needit.domain.board.donation.entity.DonationComment;
import com.prgrms.needit.domain.board.wish.entity.DonationWishComment;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentResponse {

	private Long id;
	private String comment;
	private Long userId;
	private String userName;
	private String userEmail;
	private String userImage;
	private String userRole;
	private LocalDateTime createdDate;
	private LocalDateTime updatedDate;

	private CommentResponse(
		Long id,
		String comment,
		Long userId,
		String userName,
		String userEmail,
		String userImage,
		String userRole,
		LocalDateTime createdDate,
		LocalDateTime updatedDate
	) {
		this.id = id;
		this.comment = comment;
		this.userId = userId;
		this.userName = userName;
		this.userEmail = userEmail;
		this.userImage = userImage;
		this.userRole = userRole;
		this.createdDate = createdDate;
		this.updatedDate = updatedDate;
	}

	public static CommentResponse toResponse(DonationComment comment) {
		return new CommentResponse(
			comment.getId(),
			comment.getComment(),
			comment.getCenter()
				   .getId(),
			comment.getCenter()
				   .getName(),
			comment.getCenter()
				   .getEmail(),
			comment.getCenter()
				   .getProfileImageUrl(),
			UserType.CENTER.name(),
			comment.getCreatedAt(),
			comment.getUpdatedAt()
		);
	}

	public static CommentResponse toResponse(DonationWishComment comment) {
		return new CommentResponse(
			comment.getId(),
			comment.getComment(),
			comment.getMember()
				   .getId(),
			comment.getMember()
				   .getNickname(),
			comment.getMember()
				   .getEmail(),
			comment.getMember()
				   .getProfileImageUrl(),
			UserType.MEMBER.name(),
			comment.getCreatedAt(),
			comment.getUpdatedAt()
		);
	}

	public static CommentResponse toResponse(ActivityComment comment) {
		return new CommentResponse(
			comment.getId(),
			comment.getComment(),
			comment.getWriterInfo()
				   .getWriterId(),
			comment.getWriterInfo()
				   .getWriterName(),
			comment.getWriterInfo()
				   .getProfileImageUrl(),
			comment.getWriterInfo()
				   .getWriterName(),
			comment.getWriterInfo()
				   .getWriterType()
				   .name(),
			comment.getCreatedAt(),
			comment.getUpdatedAt()
		);
	}
}
