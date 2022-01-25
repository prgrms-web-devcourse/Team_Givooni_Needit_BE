package com.prgrms.needit.domain.contract.entity;

import com.prgrms.needit.common.domain.entity.BaseEntity;
import com.prgrms.needit.domain.board.donation.entity.Donation;
import com.prgrms.needit.domain.board.wish.entity.DonationWish;
import com.prgrms.needit.domain.center.entity.Center;
import com.prgrms.needit.domain.contract.entity.enums.ContractStatus;
import com.prgrms.needit.domain.member.entity.Member;
import com.prgrms.needit.domain.message.entity.ChatMessage;
import java.time.LocalDateTime;
import javax.persistence.CascadeType;
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
@Table(name = "contract")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Contract extends BaseEntity {

	@Column(name = "date", nullable = false)
	private LocalDateTime contractDate;

	@ManyToOne
	@JoinColumn(name = "donation_id", referencedColumnName = "id")
	private Donation donation;

	@ManyToOne
	@JoinColumn(name = "wish_id", referencedColumnName = "id")
	private DonationWish donationWish;

	@ManyToOne
	@JoinColumn(name = "center_id", referencedColumnName = "id")
	private Center center;

	@ManyToOne
	@JoinColumn(name = "member_id", referencedColumnName = "id")
	private Member member;

	// use chat message's direction to specify who requested and who can aceept/refuse.
	@OneToOne(mappedBy = "contract", cascade = CascadeType.PERSIST)
	private ChatMessage chatMessage;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private ContractStatus status;

	@Builder
	public Contract(
		LocalDateTime contractDate,
		Donation donation,
		DonationWish donationWish,
		Center center,
		Member member,
		ChatMessage chatMessage,
		ContractStatus status
	) {
		validateInfo(contractDate, donation, donationWish, center, member, chatMessage, status);
		this.contractDate = contractDate;
		this.donation = donation;
		this.donationWish = donationWish;
		this.center = center;
		this.member = member;
		chatMessage.registerContract(this);
		this.chatMessage = chatMessage;
		this.status = status;
	}

	public void acceptRequest() {
		if (!ContractStatus.REQUESTED.equals(this.status)) {
			throw new IllegalArgumentException(
				"Cannot accept already accepted or cancelled contract.");
		}
		this.status = ContractStatus.ACCEPTED;
	}

	public void refuseRequest() {
		if (!ContractStatus.REQUESTED.equals(this.status)) {
			throw new IllegalArgumentException(
				"Cannot refuse already accepted or cancelled contract.");
		}
		this.status = ContractStatus.REFUSED;
	}

	private void validateInfo(
		LocalDateTime contractDate,
		Donation donation,
		DonationWish donationWish,
		Center center,
		Member member,
		ChatMessage chatMessage,
		ContractStatus status
	) {
		Assert.notNull(contractDate, "Contract date cannot be null.");
		Assert.isTrue(
			contractDate.isAfter(LocalDateTime.now()),
			"Contract date cannot be past."
		);
		Assert.isTrue(
			(donation != null && donationWish == null) ||
				(donationWish != null && donation == null),
			"Contract must reference either donation or wish article."
		);
		Assert.notNull(center, "Center cannot be null.");
		Assert.notNull(member, "Member cannot be null.");
		Assert.notNull(chatMessage, "Chat message cannot be null.");
		Assert.notNull(status, "Contract status cannot be null.");
	}

	public boolean isValid() {
		return this.status.equals(ContractStatus.ACCEPTED) ||
			this.status.equals(ContractStatus.REQUESTED);
	}
}
