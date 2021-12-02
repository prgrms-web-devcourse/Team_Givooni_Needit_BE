package com.prgrms.needit.domain.board.activity.entity;

import com.prgrms.needit.common.domain.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
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
public class ActivityImages extends BaseEntity {

	@Column(name = "url", length = 512, nullable = false)
	private String url;

	@ManyToOne
	@JoinColumn(name = "activity_id", referencedColumnName = "id")
	private Activity activity;

	private ActivityImages(String url, Activity activity) {
		this.url = url;
		this.activity = activity;
	}

	public static ActivityImages registerImage(
		String url,
		Activity activity
	) {
		return new ActivityImages(url, activity);
	}
}
