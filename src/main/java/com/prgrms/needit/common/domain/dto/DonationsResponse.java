package com.prgrms.needit.common.domain.dto;

import com.prgrms.needit.common.enums.BoardType;
import com.prgrms.needit.domain.board.donation.entity.Donation;
import com.prgrms.needit.domain.board.wish.entity.DonationWish;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DonationsResponse {

	private Long id;
	private String title;
	private String content;
	private String category;
	private String status;
	private String boardType;
	private Long userId;
	private String userName;
	private String userAddress;
	private LocalDateTime createdDate;
	private List<String> tags = new ArrayList<>();

	private DonationsResponse(
		Long id,
		String title,
		String content,
		String category,
		String status,
		Long userId,
		String userName,
		String userAddress,
		LocalDateTime createdDate,
		List<String> tags,
		String boardType
	) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.category = category;
		this.status = status;
		this.userId = userId;
		this.boardType = boardType;
		this.userName = userName;
		this.userAddress = userAddress;
		this.createdDate = createdDate;
		this.tags = tags;
	}

	public static DonationsResponse toResponse(Donation donation) {
		return new DonationsResponse(
			donation.getId(),
			donation.getTitle(),
			donation.getContent(),
			donation.getCategory()
					.getType(),
			donation.getStatus()
					.getType(),
			donation.getMember()
					.getId(),
			donation.getMember()
					.getNickname(),
			donation.getMember()
					.getAddress(),
			donation.getCreatedAt(),
			donation.getTags()
					.stream()
					.map(donationTag -> donationTag.getThemeTag()
												   .getTagName())
					.collect(Collectors.toList()),
			BoardType.DONATION.name()
		);
	}

	public static DonationsResponse toResponse(DonationWish wish) {
		return new DonationsResponse(
			wish.getId(),
			wish.getTitle(),
			wish.getContent(),
			wish.getCategory()
				.getType(),
			wish.getStatus()
				.getType(),
			wish.getCenter()
				.getId(),
			wish.getCenter()
				.getName(),
			wish.getCenter()
				.getAddress(),
			wish.getCreatedAt(),
			wish.getTags()
				.stream()
				.map(donationWishTag -> donationWishTag.getThemeTag()
													   .getTagName())
				.collect(Collectors.toList()),
			BoardType.WISH.name()
		);
	}
}
