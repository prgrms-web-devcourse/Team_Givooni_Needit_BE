package com.prgrms.needit.domain.board.wish.dto;

import com.prgrms.needit.common.enums.DonationCategory;
import com.prgrms.needit.common.enums.DonationStatus;
import com.prgrms.needit.domain.board.wish.entity.DonationWish;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DonationWishRequest {

	@NotBlank
	private String title;

	@NotBlank
	private String content;

	@NotBlank
	private String category;

	private List<Long> tags = new ArrayList<>();

	public DonationWishRequest(
		String title,
		String content,
		String category,
		List<Long> tags
	) {
		this.title = title;
		this.content = content;
		this.category = category;
		this.tags = tags;
	}

	public DonationWish toEntity() {
		return DonationWish.builder()
						   .title(title)
						   .content(content)
						   .category(DonationCategory.of(category))
						   .status(DonationStatus.DONATING)
						   .build();
	}

}