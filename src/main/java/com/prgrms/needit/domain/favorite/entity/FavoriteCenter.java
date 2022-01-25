package com.prgrms.needit.domain.favorite.entity;

import com.prgrms.needit.domain.user.entity.Users;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
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
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.Assert;

@Getter
@Entity
@Table(name = "favorite_center")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FavoriteCenter {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@CreatedDate
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "from_user_id", referencedColumnName = "id")
	private Users member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "to_user_id", referencedColumnName = "id")
	private Users center;

	private FavoriteCenter(Users member, Users center) {
		this.member = member;
		this.center = center;
	}

	private static void validateInfo(Users member, Users center) {
		Assert.notNull(member, "Member cannot be null.");
		Assert.notNull(center, "Center cannot be null.");
	}

	public static FavoriteCenter createFavCenter(Users member, Users center) {
		validateInfo(member, center);
		return new FavoriteCenter(member, center);
	}

}
