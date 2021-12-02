package com.prgrms.needit.domain.board.donation.entity;

import com.prgrms.needit.common.domain.BaseEntity;
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
	private DonationTag donationTag;

	public DonationHaveTag(
		Donation donation,
		DonationTag donationTag
	) {
		validateInfo(donation, donationTag);
		this.donation = donation;
		this.donationTag = donationTag;
	}

	public static DonationHaveTag registerDonationTag(Donation donation, DonationTag donationTag) {
		validateInfo(donation, donationTag);
		return new DonationHaveTag(donation, donationTag);
	}

	private static void validateInfo(Donation donation, DonationTag donationTag) {
		Assert.notNull(donation, "Donation cannot be null.");
		Assert.notNull(donationTag, "Donation tag cannot be null.");
	}
}
