package com.prgrms.needit.domain.message.entity;

import com.prgrms.needit.common.domain.BaseEntity;
import com.prgrms.needit.common.domain.enums.UserType;
import com.prgrms.needit.domain.board.donation.entity.DonationComment;
import com.prgrms.needit.domain.board.wish.entity.DonationWishComment;
import com.prgrms.needit.domain.center.entity.Center;
import com.prgrms.needit.domain.contract.entity.Contract;
import com.prgrms.needit.domain.member.entity.Member;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

@Getter
@Entity
@Table(name = "message_contract")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage extends BaseEntity {

	@Column(name = "content", nullable = false, length = 1024)
	private String content;

	@ManyToOne
	@JoinColumn(name = "center_id", referencedColumnName = "id")
	private Center center;

	@ManyToOne
	@JoinColumn(name = "member_id", referencedColumnName = "id")
	private Member member;

	@ManyToOne
	@JoinColumn(name = "donation_comment_id", referencedColumnName = "id")
	private DonationComment donationComment;

	@ManyToOne
	@JoinColumn(name = "donation_wish_comment_id", referencedColumnName = "id")
	private DonationWishComment donationWishComment;

	@OneToOne
	private Contract contract;

	@Enumerated(EnumType.STRING)
	@Column(name = "senderType", nullable = false)
	private UserType senderType;

	public void registerContract(Contract contract) {
		Assert.isNull(this.contract, "Contract already registered.");
		this.contract = contract;
	}

	@Builder
	public ChatMessage(
		String content,
		Center center,
		Member member,
		UserType senderType,
		DonationComment donationComment,
		DonationWishComment donationWishComment,
		Contract contract
	) {
		validateInfo(
			content, center, member, donationComment, donationWishComment, senderType);
		this.content = content;
		this.center = center;
		this.member = member;
		this.donationComment = donationComment;
		this.senderType = senderType;
		this.donationWishComment = donationWishComment;
		this.contract = contract;
	}

	private void validateInfo(
		String content,
		Center center,
		Member member,
		DonationComment donationComment,
		DonationWishComment donationWishComment,
		UserType senderType
	) {
		Assert.hasText(content, "Chat message cannot be null or empty.");
		Assert.notNull(center, "Center cannot be null.");
		Assert.notNull(member, "Member cannot be null.");
		Assert.isTrue(
			(donationComment != null && donationWishComment == null) ||
				(donationComment == null && donationWishComment != null),
			"Chat message must reference either donation or wish comment."
		);
		Assert.notNull(senderType, "Message's direction cannot be null.");
	}

}