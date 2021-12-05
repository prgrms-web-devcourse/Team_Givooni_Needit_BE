package com.prgrms.needit.domain.board.wish.entity;

import com.prgrms.needit.common.domain.entity.BaseEntity;
import com.prgrms.needit.common.domain.entity.ThemeTag;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "wish_have_tag")
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class DonationWishHaveTag extends BaseEntity {

	@ManyToOne
	@JoinColumn(name = "wish_id", referencedColumnName = "id")
	private DonationWish donationWish;

	@ManyToOne
	@JoinColumn(name = "tag_id", referencedColumnName = "id")
	private ThemeTag themeTag;

	private DonationWishHaveTag(
		DonationWish donationWish,
		ThemeTag themeTag
	) {
		this.donationWish = donationWish;
		this.themeTag = themeTag;
	}

	public static DonationWishHaveTag registerWishTag(
		DonationWish donationWish,
		ThemeTag themeTag) {

		return new DonationWishHaveTag(donationWish, themeTag);
	}

}
