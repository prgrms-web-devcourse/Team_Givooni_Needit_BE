package com.prgrms.needit.domain.board.donation.dto;

import com.prgrms.needit.common.enums.DonationCategory;
import com.prgrms.needit.common.enums.DonationQuality;
import com.prgrms.needit.common.enums.DonationStatus;
import com.prgrms.needit.domain.board.donation.entity.Donation;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DonationRequest {

	@NotBlank
	private String title;

	@NotBlank
	private String content;

	@NotBlank
	private String category;

	@NotBlank
	private String quality;

	private List<Long> tags = new ArrayList<>();

	public DonationRequest(
		String title,
		String content,
		String category,
		String quality,
		List<Long> tags
	) {
		this.title = title;
		this.content = content;
		this.category = category;
		this.quality = quality;
		this.tags = tags;
	}

	public Donation toEntity() {
		return Donation.builder()
					   .title(title)
					   .content(content)
					   .category(DonationCategory.of(category))
					   .quality(DonationQuality.of(quality))
					   .status(DonationStatus.DONATING)
					   .build();
	}

}
