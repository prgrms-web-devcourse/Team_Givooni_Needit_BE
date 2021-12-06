package com.prgrms.needit.domain.board.donation.entity;

import com.prgrms.needit.common.domain.entity.BaseEntity;
import com.prgrms.needit.domain.center.entity.Center;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.*;

@Getter
@Entity
@Table(name = "donation_comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DonationComment extends BaseEntity {

	@Column(name = "comment", length = 512, nullable = false)
	private String comment;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "donation_id", referencedColumnName = "id")
	private Donation donation;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "center_id", referencedColumnName = "id")
	private Center center;

	@Builder
	private DonationComment(
		String comment,
		Donation donation,
		Center center
	) {
		this.comment = comment;
		this.donation = donation;
		this.center = center;
	}

}
