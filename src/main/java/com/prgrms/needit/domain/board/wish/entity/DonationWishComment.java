package com.prgrms.needit.domain.board.wish.entity;

import com.prgrms.needit.common.domain.entity.BaseEntity;
import com.prgrms.needit.domain.member.entity.Member;
import javax.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "wish_comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DonationWishComment extends BaseEntity {

	@Column(name = "comment", length = 512, nullable = false)
	private String comment;

	@ManyToOne
	@JoinColumn(name = "donation_wish_id", referencedColumnName = "id")
	private DonationWish donationWish;

	@OneToOne
	@JoinColumn(name = "member_id", referencedColumnName = "id")
	private Member member;

	@Builder
	private DonationWishComment(
		String comment,
		DonationWish donationWish,
		Member member
	) {
		this.comment = comment;
		this.donationWish = donationWish;
		this.member = member;
	}

}
