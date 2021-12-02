package com.prgrms.needit.domain.member.entity;

import com.prgrms.needit.common.domain.BaseEntity;
import com.prgrms.needit.domain.center.entity.Center;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

@Getter
@Entity
@Table(name = "favorite_center")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FavoriteCenter extends BaseEntity {

	@ManyToOne
	@JoinColumn(name = "member_id", referencedColumnName = "id")
	private Member member;

	@ManyToOne
	@JoinColumn(name = "center_id", referencedColumnName = "id")
	private Center center;

	private FavoriteCenter(Member member, Center center) {
		this.member = member;
		this.center = center;
	}

	public static FavoriteCenter favorCenter(Member member, Center center) {
		validateInfo(member, center);
		return new FavoriteCenter(member, center);
	}

	private static void validateInfo(Member member, Center center) {
		Assert.notNull(member, "Member cannot be null.");
		Assert.notNull(center, "Center cannot be null.");
	}

}
