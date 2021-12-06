package com.prgrms.needit.domain.board.donation.entity;

import com.prgrms.needit.common.domain.entity.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "donation_image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DonationImage extends BaseEntity {

	@Column(name = "url", length = 512, nullable = false)
	private String url;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "donation_id", referencedColumnName = "id")
	private Donation donation;

	private DonationImage(String url, Donation donation) {
		this.url = url;
		this.donation = donation;
	}

	public static DonationImage registerImage(
		String url,
		Donation donation
	) {
		return new DonationImage(url, donation);
	}

}
