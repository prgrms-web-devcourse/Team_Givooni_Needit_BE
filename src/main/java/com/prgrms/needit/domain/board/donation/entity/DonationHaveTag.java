package com.prgrms.needit.domain.board.donation.entity;

import com.prgrms.needit.common.domain.BaseEntity;
import com.prgrms.needit.common.domain.ThemeTag;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "donation_have_tag")
public class DonationHaveTag extends BaseEntity {

	@ManyToOne
	@JoinColumn(name = "donation_id", referencedColumnName = "id")
	private Donation donation;

	@ManyToOne
	@JoinColumn(name = "tag_id", referencedColumnName = "id")
	private ThemeTag themeTag;

	public DonationHaveTag(
		Donation donation,
		ThemeTag themeTag
	) {
		this.donation = donation;
		this.themeTag = themeTag;
	}

	public static DonationHaveTag registerDonationTag(
		Donation donation,
		ThemeTag themeTag) {

		return new DonationHaveTag(donation, themeTag);
	}

}
