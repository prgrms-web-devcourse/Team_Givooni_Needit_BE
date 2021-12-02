package com.prgrms.needit.domain.board.wish.entity;

import com.prgrms.needit.common.domain.BaseEntity;
import com.prgrms.needit.domain.board.donation.entity.DonationTag;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "wish_tag")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DonationWishTag extends BaseEntity {

	@Column(name = "name", length = 32, nullable = false, unique = true)
	private String tagName;

	private DonationWishTag(String tagName) {
		this.tagName = tagName;
	}

	public static DonationWishTag registerTag(String tagName) {
		return new DonationWishTag(tagName);
	}
}
