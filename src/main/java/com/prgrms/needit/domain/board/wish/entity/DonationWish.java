package com.prgrms.needit.domain.board.wish.entity;

import com.prgrms.needit.common.domain.BaseEntity;
import com.prgrms.needit.common.domain.enums.DonationCategory;
import com.prgrms.needit.common.domain.enums.DonationStatus;
import com.prgrms.needit.domain.board.donation.entity.DonationHaveTag;
import com.prgrms.needit.domain.board.donation.entity.DonationTag;
import com.prgrms.needit.domain.center.entity.Center;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "wish_board")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DonationWish extends BaseEntity {

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "content", nullable = false)
	private String content;

	@Enumerated(EnumType.STRING)
	@Column(name = "category", nullable = false)
	private DonationCategory category;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private DonationStatus status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "center_id", referencedColumnName = "id")
	private Center center;

	@OneToMany(mappedBy = "wish_board", cascade = CascadeType.ALL)
	private List<DonationWishHaveTag> tags = new ArrayList<>();

	@OneToMany(mappedBy = "wish_board", cascade = CascadeType.ALL)
	private List<DonationWishComment> comments = new ArrayList<>();

	@Builder
	private DonationWish(
		String title,
		String content,
		DonationCategory category,
		DonationStatus status,
		Center center
	) {
		validateInfo(title, content, category);
		validateStatus(status);

		this.title = title;
		this.content = content;
		this.category = category;
		this.status = status;
		this.center = center;
	}

	private void validateInfo(String title, String content, DonationCategory category) {
		Assert.hasText(title, "Updated title cannot be null or blank.");
		Assert.hasText(content, "Updated content cannot be null or blank.");
		Assert.notNull(category, "Updated category cannot be null or blank.");
	}

	private void validateStatus(DonationStatus status) {
		Assert.notNull(status, "Donation wish status cannot be null or blank.");
	}

	public void changeInfo(String title, String content, DonationCategory category) {
		validateInfo(title, content, category);

		this.title = title;
		this.content = content;
		this.category = category;
	}

	public void changeStatus(DonationStatus status) {
		validateStatus(status);

		this.status = status;
	}

	public void addTag(DonationWishHaveTag tag) {
		this.getTags().add(tag);
	}

}
