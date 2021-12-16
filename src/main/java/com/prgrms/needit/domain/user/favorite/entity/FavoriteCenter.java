package com.prgrms.needit.domain.user.favorite.entity;

import com.prgrms.needit.domain.user.center.entity.Center;
import com.prgrms.needit.domain.user.member.entity.Member;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.util.Assert;

@Getter
@Entity
@Table(name = "favorite_center")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FavoriteCenter {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@CreatedDate
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@ManyToOne
	@JoinColumn(name = "member_id", referencedColumnName = "id")
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "center_id", referencedColumnName = "id")
	private Center center;

	private FavoriteCenter(Member member, Center center) {
		this.member = member;
		this.center = center;
	}

	public static FavoriteCenter createFavCenter(Member member, Center center) {
		validateInfo(member, center);
		return new FavoriteCenter(member, center);
	}

	private static void validateInfo(Member member, Center center) {
		Assert.notNull(member, "Member cannot be null.");
		Assert.notNull(center, "Center cannot be null.");
	}

}
