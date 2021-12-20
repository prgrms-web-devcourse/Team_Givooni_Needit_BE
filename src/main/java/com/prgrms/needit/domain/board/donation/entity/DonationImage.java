package com.prgrms.needit.domain.board.donation.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
public class DonationImage {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

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
