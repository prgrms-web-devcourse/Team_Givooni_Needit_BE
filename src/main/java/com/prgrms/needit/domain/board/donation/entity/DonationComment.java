package com.prgrms.needit.domain.board.donation.entity;

import com.prgrms.needit.common.domain.entity.BaseEntity;
import com.prgrms.needit.domain.center.entity.Center;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.*;
import org.springframework.util.Assert;

@Getter
@Entity
@Table(name = "donation_comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DonationComment extends BaseEntity {

	@Column(name = "comment", length = 512, nullable = false)
	private String comment;

	@ManyToOne
	@JoinColumn(name = "donation_id", referencedColumnName = "id")
	private Donation donation;

	@ManyToOne
	@JoinColumn(name = "center_id", referencedColumnName = "id")
	private Center center;

	public void changeComment(String comment) {
		validateInfo(comment, donation, center);
		this.comment = comment;
	}

	@Builder
	private DonationComment(
		String comment,
		Donation donation,
		Center center
	) {
		validateInfo(comment, donation, center);
		this.comment = comment;
		this.donation = donation;
		this.center = center;
	}

	private static void validateInfo(String comment, Donation donation, Center center) {
		Assert.hasText(comment, "Comment cannot be null or empty.");
		Assert.notNull(donation, "Donation cannot be null.");
		Assert.notNull(center, "Center cannot be null.");
	}

}
