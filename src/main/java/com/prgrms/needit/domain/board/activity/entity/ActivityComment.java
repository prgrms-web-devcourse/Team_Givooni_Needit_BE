package com.prgrms.needit.domain.board.activity.entity;

import com.prgrms.needit.common.domain.entity.BaseEntity;
import com.prgrms.needit.common.enums.UserType;
import java.util.Objects;
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
		validateComment(comment, activity, writerId, writerType);
		this.comment = comment;
		this.activity = activity;
		this.activity.addComment(this);
		this.writerId = writerId;
		this.writerType = writerType;
	}

	private void validateComment(
		String comment,
		Activity activity,
		Long writerId,
		UserType writerType
	) {
		Assert.hasText(comment, "Comment cannot be null or empty.");
		Assert.notNull(activity, "Activity cannot be null on comment.");
		Assert.notNull(writerId, "Writer's id cannot be null.");
		Assert.notNull(writerType, "Writer type cannot be null.");
	}

	public void changeComment(String comment) {
		Assert.hasText(comment, "Comment cannot be null or empty.");
		this.comment = comment;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ActivityComment that = (ActivityComment) o;
		return comment.equals(that.comment) && activity.equals(that.activity) && writerId.equals(
			that.writerId) && writerType == that.writerType;
	}

	@Override
	public int hashCode() {
		return Objects.hash(comment, activity, writerId, writerType);
	}
}
