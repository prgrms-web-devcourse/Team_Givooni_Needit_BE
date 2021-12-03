package com.prgrms.needit.domain.message.entity;

import com.prgrms.needit.common.domain.BaseEntity;
import com.prgrms.needit.common.enums.UserType;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "message_contract")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageContract extends BaseEntity {

	@Column(name = "date", nullable = false)
	private LocalDateTime date;

	@Column(name = "receiver_id", nullable = false)
	private Long receiverId;

	@Enumerated(EnumType.STRING)
	@Column(name = "receiver_type", nullable = false)
	private UserType receiverType;

	@Column(name = "sender_id", nullable = false)
	private Long senderId;

	@Enumerated(EnumType.STRING)
	@Column(name = "sender_type", nullable = false)
	private UserType senderType;

	@Column(name = "post_id", nullable = false)
	private Long postId;

	@Enumerated(EnumType.STRING)
	@Column(name = "post_type", nullable = false)
	private PostType postType;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private ContractStatus status;

	@Enumerated(EnumType.STRING)
	@Column(name = "type", nullable = false)
	private MessageType messageType;

	@Builder
	public MessageContract(
		LocalDateTime date,
		Long receiverId,
		UserType receiverType,
		Long senderId,
		UserType senderType,
		Long postId,
		PostType postType,
		ContractStatus status,
		MessageType messageType
	) {
		this.date = date;
		this.receiverId = receiverId;
		this.receiverType = receiverType;
		this.senderId = senderId;
		this.senderType = senderType;
		this.postId = postId;
		this.postType = postType;
		this.status = status;
		this.messageType = messageType;
	}
}
