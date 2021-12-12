package com.prgrms.needit.domain.message.entity;

import com.prgrms.needit.common.domain.entity.BaseEntity;
import com.prgrms.needit.common.enums.UserType;
import com.prgrms.needit.domain.board.donation.entity.Donation;
import com.prgrms.needit.domain.board.wish.entity.DonationWish;
import com.prgrms.needit.domain.contract.entity.Contract;
import com.prgrms.needit.domain.user.center.entity.Center;
import com.prgrms.needit.domain.user.member.entity.Member;
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
@Table(name = "chat_message")
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
	@JoinColumn(name = "donation_id", referencedColumnName = "id")
	private Donation donation;

	@ManyToOne
	@JoinColumn(name = "donation_wish_id", referencedColumnName = "id")
	private DonationWish donationWish;

	@OneToOne
	private Contract contract;

	@Enumerated(EnumType.STRING)
	@Column(name = "senderType", nullable = false)
	private UserType senderType;

	@Builder
	public ChatMessage(
		String content,
		Center center,
		Member member,
		UserType senderType,
		Donation donation,
		DonationWish donationWish,
		Contract contract
	) {
		validateInfo(
			content, center, member, donation, donationWish, senderType);
		this.content = content;
		this.center = center;
		this.member = member;
		this.donation = donation;
		this.senderType = senderType;
		this.donationWish = donationWish;
		this.contract = contract;
	}

	public void registerContract(Contract contract) {
		Assert.isNull(this.contract, "Contract already registered.");
		this.contract = contract;
	}

	private void validateInfo(
		String content,
		Center center,
		Member member,
		Donation donation,
		DonationWish donationWish,
		UserType senderType
	) {
		Assert.hasText(content, "Chat message cannot be null or empty.");
		Assert.notNull(center, "Center cannot be null.");
		Assert.notNull(member, "Member cannot be null.");
		Assert.isTrue(
			(donation != null && donationWish == null) ||
				(donation == null && donationWish != null),
			"Chat message must reference either donation or wish."
		);
		Assert.notNull(senderType, "Message's direction cannot be null.");
	}

}
