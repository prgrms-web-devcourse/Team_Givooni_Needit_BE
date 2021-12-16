package com.prgrms.needit.domain.board.activity.entity;

import com.prgrms.needit.common.domain.entity.BaseEntity;
import com.prgrms.needit.domain.board.activity.entity.enums.ActivityType;
import com.prgrms.needit.domain.user.center.entity.Center;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

@Getter
@Entity
@Table(name = "activity_board")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Activity extends BaseEntity {

	@Column(name = "activity_type", nullable = false)
	private ActivityType activityType;

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "content", nullable = false)
	private String content;

	@ManyToOne
	@JoinColumn(name = "center_id", referencedColumnName = "id")
	private Center center;

	@OneToMany(mappedBy = "activity", cascade = CascadeType.PERSIST)
	private final List<ActivityImage> images = new ArrayList<>();

	@OneToMany(mappedBy = "activity", cascade = CascadeType.PERSIST)
	private final List<ActivityComment> comments = new ArrayList<>();

	public void addComment(ActivityComment comment) {
		comments.add(comment);
	}

	public void removeComment(ActivityComment comment) {
		comments.remove(comment);
	}

	public void addImage(ActivityImage image) {
		images.add(image);
	}

	public void removeImage(ActivityImage image) {
		images.remove(image);
	}

	@Builder
	private Activity(
		String title,
		String content,
		Center center,
		ActivityType activityType
	) {
		validateInfo(title, content, center, activityType);
		this.title = title;
		this.content = content;
		this.center = center;
		this.activityType = activityType;
	}

	private void validateInfo(
		String title,
		String content,
		Center center,
		ActivityType activityType
	) {
		Assert.hasText(title, "Updated title cannot be null or empty.");
		Assert.hasText(content, "Updated content cannot be null or empty.");
		Assert.notNull(center, "Center cannot be null.");
		Assert.notNull(activityType, "Updated activity type cannot be null or empty.");
	}

	public void changeInfo(String title, String content, ActivityType activityType) {
		validateInfo(title, content, this.center, activityType);
		this.title = title;
		this.content = content;
		this.activityType = activityType;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Activity activity = (Activity) o;
		return activityType == activity.activityType && title.equals(activity.title)
			&& content.equals(
			activity.content) && center.equals(activity.center) && images.equals(activity.images)
			&& comments.equals(activity.comments);
	}

	@Override
	public int hashCode() {
		return Objects.hash(activityType, title, content, center, images, comments);
	}
}
