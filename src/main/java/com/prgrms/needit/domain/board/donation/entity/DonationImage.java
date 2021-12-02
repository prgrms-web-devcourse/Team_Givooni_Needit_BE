package com.prgrms.needit.domain.board.donation.entity;

import com.prgrms.needit.common.domain.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

@Getter
@Table(name = "donation_image")
@Entity
@NoArgsConstructor
public class DonationImage extends BaseEntity {

	@ManyToOne
	@JoinColumn(name = "donation_id", referencedColumnName = "id")
	private Donation donation;

	@Column(name = "url", length = 512, nullable = false)
	private String url;

	@Builder
	public DonationImage(Donation donation, String url) {
		Assert.notNull(donation, "Donation cannot be null.");
		Assert.hasText(url, "Image url cannot be null or blank.");
		this.donation = donation;
		this.url = url;
	}
}
