package com.prgrms.needit.domain.board.donation.entity;

import com.prgrms.needit.common.domain.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "donation_tag")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DonationTag extends BaseEntity {

	@Column(name = "name", length = 32, nullable = false, unique = true)
	private String tagName;

	private DonationTag(String tagName) {
		this.tagName = tagName;
	}

	public static DonationTag registerTag(String tagName) {
		return new DonationTag(tagName);
	}

}
