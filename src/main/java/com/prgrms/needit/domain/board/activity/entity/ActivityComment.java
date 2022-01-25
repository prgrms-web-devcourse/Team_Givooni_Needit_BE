package com.prgrms.needit.domain.board.activity.entity;

import com.prgrms.needit.common.domain.entity.BaseEntity;
import com.prgrms.needit.common.error.ErrorCode;
import com.prgrms.needit.common.error.exception.InvalidArgumentException;
import com.prgrms.needit.domain.board.activity.dto.ActivityCommentWriterInfo;
import com.prgrms.needit.domain.center.entity.Center;
import com.prgrms.needit.domain.member.entity.Member;
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

	@ManyToOne
	@JoinColumn(name = "member_id", referencedColumnName = "id")
	private Member member;

	@ManyToOne
	@JoinColumn(name = "center_id", referencedColumnName = "id")
	private Center center;

	@Builder
	public ActivityComment(
		String comment,
		Activity activity,
		Member member,
		Center center
	) {
		this.comment = comment;
		this.activity = activity;
		this.activity.addComment(this);
		if (member != null && center == null) {
			validateComment(comment, activity, member);
			this.member = member;
			return;
		}

		if (member == null && center != null) {
			validateComment(comment, activity, center);
			this.center = center;
			return;
		}

		throw new InvalidArgumentException(ErrorCode.INVALID_INPUT_VALUE);
	}

	private static void validateComment(
		String comment,
		Activity activity
	) {
		Assert.hasText(comment, "Comment cannot be null or empty.");
		Assert.notNull(activity, "Activity cannot be null on comment.");
	}

	private static void validateComment(
		String comment,
		Activity activity,
		Member member
	) {
		validateComment(comment, activity);
		Assert.notNull(member, "Member cannot be null.");
	}

	private static void validateComment(
		String comment,
		Activity activity,
		Center center
	) {
		validateComment(comment, activity);
		Assert.notNull(center, "Center cannot be null.");
	}

	public ActivityCommentWriterInfo getWriterInfo() {
		if (member != null && center == null) {
			return ActivityCommentWriterInfo.ofMember(member);
		} else if (member == null && center != null) {
			return ActivityCommentWriterInfo.ofCenter(center);
		} else {
			return null;
		}
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
		return comment.equals(that.comment) && activity.equals(that.activity) && member.equals(
			that.member) && center.equals(that.center);
	}

	@Override
	public int hashCode() {
		return Objects.hash(comment, activity, member, center);
	}

}
