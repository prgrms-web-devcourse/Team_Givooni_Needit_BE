package com.prgrms.needit.domain.board.donation.entity;

import com.prgrms.needit.common.domain.entity.BaseEntity;
import com.prgrms.needit.common.domain.entity.ThemeTag;
import com.prgrms.needit.common.enums.DonationCategory;
import com.prgrms.needit.common.enums.DonationQuality;
import com.prgrms.needit.common.enums.DonationStatus;
import com.prgrms.needit.domain.board.donation.dto.DonationRequest;
import com.prgrms.needit.domain.user.member.entity.Member;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", referencedColumnName = "id")
	private Member member;

	@OneToMany(mappedBy = "donation", cascade = CascadeType.ALL)
	private List<DonationHaveTag> tags = new ArrayList<>();

	@OneToMany(mappedBy = "donation", cascade = CascadeType.ALL)
	private List<DonationComment> comments = new ArrayList<>();

	@OneToMany(mappedBy = "donation", cascade = CascadeType.ALL)
	private List<DonationImage> images = new ArrayList<>();

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

	public void changeInfo(
		DonationRequest request
	) {
		validateInfo(
			request.getTitle(),
			request.getContent(),
			DonationCategory.of(request.getCategory()),
			DonationQuality.of(request.getQuality())
		);

		this.title = request.getTitle();
		this.content = request.getContent();
		this.category = DonationCategory.of(request.getCategory());
		this.quality = DonationQuality.of(request.getQuality());
	}

	public void changeStatus(DonationStatus status) {
		validateStatus(status);
		this.status = status;
	}

	public void addMember(Member member) {
		this.member = member;
	}

	public void addTag(ThemeTag tag) {
		this.tags.add(buildHaveTag(tag));
	}

	public void addComment(DonationComment donationComment) {
		this.comments.add(donationComment);
	}

	public void addImage(DonationImage image) {
		this.images.add(image);
	}

	private DonationHaveTag buildHaveTag(ThemeTag tag) {
		return DonationHaveTag.registerDonationTag(this, tag);
	}

	private void validateInfo(
		String title,
		String content,
		DonationCategory category,
		DonationQuality quality
	) {
		Assert.hasText(title, "Updated title cannot be null or empty.");
		Assert.hasText(content, "Updated content cannot be null or empty.");
		Assert.notNull(category, "Updated category cannot be null or empty.");
		Assert.notNull(quality, "Updated quality cannot be null or empty.");
	}

	private void validateStatus(DonationStatus status) {
		Assert.notNull(status, "Donation status cannot be null or empty.");
	}

}
