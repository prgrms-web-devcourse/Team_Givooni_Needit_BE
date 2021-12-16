package com.prgrms.needit.domain.board.wish.dto;

import com.prgrms.needit.common.domain.dto.CommentResponse;
import com.prgrms.needit.common.enums.BoardType;
import com.prgrms.needit.domain.board.wish.entity.DonationWish;
import com.prgrms.needit.domain.board.wish.entity.DonationWishImage;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DonationWishResponse {

	private Long id;
	private String title;
	private String content;
	private String category;
	private String status;
	private Long userId;
	private String userName;
	private String userImage;
	private int userCnt;
	private String boardType;
	private LocalDateTime createdDate;
	private LocalDateTime updatedDate;
	private List<String> tags = new ArrayList<>();
	private List<String> images = new ArrayList<>();
	private List<CommentResponse> comments = new ArrayList<>();

	public DonationWishResponse(DonationWish wish) {
		this.id = wish.getId();
		this.title = wish.getTitle();
		this.content = wish.getContent();
		this.category = wish.getCategory()
							.getType();
		this.status = wish.getStatus()
						  .getType();
		this.userId = wish.getCenter()
						  .getId();
		this.userName = wish.getCenter()
							.getName();
		this.userImage = wish.getCenter()
							 .getProfileImageUrl();
		this.createdDate = wish.getCreatedAt();
		this.updatedDate = wish.getUpdatedAt();
		this.boardType = BoardType.WISH.name();
		this.tags = wish.getTags()
						.stream()
						.map(donationTag -> donationTag.getThemeTag()
													   .getTagName())
						.collect(Collectors.toList());
		this.images = wish.getImages()
						  .stream()
						  .map(DonationWishImage::getUrl)
						  .collect(Collectors.toList());
		this.comments = wish.getComments()
							.stream()
							.filter(comment -> !comment.isDeleted())
							.map(CommentResponse::toResponse)
							.collect(Collectors.toList());
		this.userCnt = this.comments.size();
	}
}
