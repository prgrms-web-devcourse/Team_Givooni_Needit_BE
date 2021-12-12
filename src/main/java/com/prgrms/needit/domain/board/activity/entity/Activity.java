package com.prgrms.needit.domain.board.activity.entity;

import com.prgrms.needit.common.domain.entity.BaseEntity;
import com.prgrms.needit.domain.user.center.entity.Center;
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

enum ActivityType {
	REVIEW,    // 활동 후기
	NOTICE    // 공지
}

@Getter
@Entity
@Table(name = "activity_board")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Activity extends BaseEntity {

	@Column(name = "activity_type", nullable = false)
	public ActivityType activityType;
	@Column(name = "title", nullable = false)
	private String title;
	@Column(name = "content", nullable = false)
	private String content;
	@ManyToOne
	@JoinColumn(name = "center_id", referencedColumnName = "id")
	private Center center;

	@Builder
	private Activity(
		String title,
		String content,
		ActivityType activityType
	) {
		validateInfo(title, content, activityType);
		this.title = title;
		this.content = content;
		this.activityType = activityType;
	}

	private void validateInfo(String title, String content, ActivityType activityType) {
		Assert.hasText(title, "Updated title cannot be null or empty.");
		Assert.hasText(content, "Updated content cannot be null or empty.");
		Assert.notNull(activityType, "Updated activity type cannot be null or empty.");
	}

	public void changeInfo(String title, String content, ActivityType activityType) {
		validateInfo(title, content, activityType);

		this.title = title;
		this.content = content;
		this.activityType = activityType;
	}
}
