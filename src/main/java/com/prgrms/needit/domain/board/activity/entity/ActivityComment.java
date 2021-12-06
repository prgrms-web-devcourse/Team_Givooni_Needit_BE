package com.prgrms.needit.domain.board.activity.entity;

import com.prgrms.needit.common.domain.BaseEntity;
import com.prgrms.needit.common.enums.UserType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

@Getter
@Entity
@Table(name = "activity_board_comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ActivityComment extends BaseEntity {

	@Column(name = "comment", length = 512, nullable = false)
	private String comment;

	@ManyToOne
	@JoinColumn(name = "activity_id", referencedColumnName = "id")
	private Activity activity;

	@Column(name = "writer_id", nullable = false)
	private Long writerId;

	@Column(name = "writer_type", nullable = false)
	private UserType writerType;

	@Builder
	private ActivityComment(
		String comment,
		Activity activity,
		Long writerId,
		UserType writerType
	) {
		validateComment(comment);

		this.comment = comment;
		this.activity = activity;
		this.writerId = writerId;
		this.writerType = writerType;
	}

	private void validateComment(String comment) {
		Assert.hasText(comment, "Comment cannot be null or empty.");
	}

	public void changeCommentInfo(String comment) {
		validateComment(comment);

		this.comment = comment;
	}

}
