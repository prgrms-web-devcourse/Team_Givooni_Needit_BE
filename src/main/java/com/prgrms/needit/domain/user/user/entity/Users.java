package com.prgrms.needit.domain.user.user.entity;

import com.prgrms.needit.common.domain.entity.BaseEntity;
import com.prgrms.needit.common.enums.UserType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@Getter
@Entity
@DynamicInsert
@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Users extends BaseEntity {

	@Column(name = "nickname", length = 64, nullable = false, unique = true)
	private String nickname;

	@Column(name = "email", length = 256, nullable = false, unique = true)
	private String email;

	@Column(name = "password", length = 128, nullable = false)
	private String password;

	@Column(name = "address", length = 128, nullable = false)
	private String address;

	@Column(name = "contact", length = 16, nullable = false)
	private String contact;

	@Column(name = "image", length = 512, nullable = false)
	private String image;

	@Column(name = "introduction", length = 200)
	private String introduction;

	@Enumerated(EnumType.STRING)
	@Column(name = "user_role", nullable = false)
	private UserType userRole;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "center_id")
	private CenterSideInfo centerMoreInfo;

	@Builder
	private Users(
		String nickname,
		String email,
		String password,
		String address,
		String contact,
		String image,
		String introduction,
		UserType userRole,
		CenterSideInfo centerSideInfo
	) {
		this.nickname = nickname;
		this.email = email;
		this.password = password;
		this.address = address;
		this.contact = contact;
		this.image = image;
		this.introduction = introduction;
		this.userRole = userRole;
		this.centerMoreInfo = centerSideInfo;
	}

	public void changeUserInfo(
		String nickname,
		String password,
		String address,
		String contact,
		String image,
		String introduction
	) {
		this.nickname = nickname;
		this.password = password;
		this.address = address;
		this.contact = contact;
		this.image = image;
		this.introduction = introduction;
	}

}
