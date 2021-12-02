package com.prgrms.needit.domain.board.donation.entity;

import com.prgrms.needit.common.domain.BaseEntity;
import com.prgrms.needit.common.domain.enums.DonationCategory;
import com.prgrms.needit.common.domain.enums.DonationQuality;
import com.prgrms.needit.common.domain.enums.DonationStatus;
import com.prgrms.needit.domain.member.entity.Member;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

@Getter
@Entity
@Table(name = "donation_board")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Donation extends BaseEntity {

	@Column(name = "title", nullable = false)
	private String title;

	@Lob
	@Column(name = "content", nullable = false)
	private String content;

	@Enumerated(EnumType.STRING)
	@Column(name = "category", nullable = false)
	private DonationCategory category;

	@Enumerated(EnumType.STRING)
	@Column(name = "quality")
	private DonationQuality quality;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private DonationStatus status;

	@ManyToOne
	@JoinColumn(name = "member_id", referencedColumnName = "id")
	private Member member;

	@Builder
	private Donation(
		String title,
		String content,
		DonationCategory category,
		DonationQuality quality,
		DonationStatus status,
		Member member
	) {
		validateInfo(title, content, category, quality);
		validateStatus(status);

		this.title = title;
		this.content = content;
		this.category = category;
		this.quality = quality;
		this.status = status;
		this.member = member;
	}

	private void validateInfo(String title, String content, DonationCategory category, DonationQuality quality) {
		Assert.hasText(title, "Updated title cannot be null or empty.");
		Assert.hasText(content, "Updated content cannot be null or empty.");
		Assert.notNull(category, "Updated category cannot be null or empty.");
		Assert.notNull(quality, "Updated quality cannot be null or empty.");
	}

	private void validateStatus(DonationStatus status) {
		Assert.notNull(status, "Donation status cannot be null or empty.");
	}

	public void changeInfo(String title, String content, DonationCategory category, DonationQuality quality) {
		validateInfo(title, content, category, quality);

		this.title = title;
		this.content = content;
		this.category = category;
		this.quality = quality;
	}

	public void changeStatus(DonationStatus status) {
		validateStatus(status);

		this.status = status;
	}

}
