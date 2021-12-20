package com.prgrms.needit.domain.board.donation.entity;

import com.prgrms.needit.common.domain.entity.ThemeTag;
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
@Table(name = "donation_have_tag")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DonationHaveTag {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "donation_id", referencedColumnName = "id")
	private Donation donation;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tag_id", referencedColumnName = "id")
	private ThemeTag themeTag;

	private DonationHaveTag(
		Donation donation,
		ThemeTag themeTag
	) {
		this.donation = donation;
		this.themeTag = themeTag;
	}

	public static DonationHaveTag registerDonationTag(
		Donation donation,
		ThemeTag themeTag
	) {
		return new DonationHaveTag(donation, themeTag);
	}

}
