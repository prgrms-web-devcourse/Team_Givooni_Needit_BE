package com.prgrms.needit.domain.board.activity.entity;

import com.prgrms.needit.common.domain.entity.BaseEntity;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "activity_board_images")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ActivityImage extends BaseEntity {

	@Column(name = "url", length = 512, nullable = false)
	private String url;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "activity_id", referencedColumnName = "id")
	private Activity activity;

	private ActivityImage(String url, Activity activity) {
		this.url = url;
		this.activity = activity;
		this.activity.addImage(this);
	}

	public static ActivityImage registerImage(
		String url,
		Activity activity
	) {
		return new ActivityImage(url, activity);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ActivityImage that = (ActivityImage) o;
		return url.equals(that.url) && activity.equals(that.activity);
	}

	@Override
	public int hashCode() {
		return Objects.hash(url, activity);
	}
}
