package com.prgrms.needit.domain.board.wish.entity;

import com.prgrms.needit.common.domain.entity.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
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
public class DonationWishImage extends BaseEntity {

	@Column(name = "url", length = 512, nullable = false)
	private String url;

	@ManyToOne
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
