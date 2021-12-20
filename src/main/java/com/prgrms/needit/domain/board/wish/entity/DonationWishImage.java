package com.prgrms.needit.domain.board.wish.entity;

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
@Table(name = "wish_image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DonationWishImage {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "url", length = 512, nullable = false)
	private String url;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "donation_wish_id", referencedColumnName = "id")
	private DonationWish donationWish;

	private DonationWishImage(String url, DonationWish donationWish) {
		this.url = url;
		this.donationWish = donationWish;
	}

	public static DonationWishImage registerImage(
		String url,
		DonationWish donationWish
	) {
		return new DonationWishImage(url, donationWish);
	}

}
