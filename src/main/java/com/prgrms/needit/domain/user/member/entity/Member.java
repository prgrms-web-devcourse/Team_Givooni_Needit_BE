package com.prgrms.needit.domain.user.member.entity;

import com.prgrms.needit.common.domain.entity.BaseEntity;
import com.prgrms.needit.common.enums.UserType;
import com.prgrms.needit.domain.user.center.entity.Center;
import com.prgrms.needit.domain.user.favorite.entity.FavoriteCenter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.util.Assert;

@Getter
@Entity
@DynamicInsert
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

	@Column(name = "email", length = 256, nullable = false, unique = true)
	private String email;

	@Column(name = "nickname", length = 64, nullable = false, unique = true)
	private String nickname;

	@Column(name = "password", length = 128, nullable = false)
	private String password;

	@Column(name = "address", length = 128, nullable = false)
	private String address;

	@Column(name = "contact", length = 16, nullable = false)
	private String contact;

	@Column(name = "image", length = 512, nullable = false)
	private String profileImageUrl;

	@Enumerated(EnumType.STRING)
	@Column(name = "user_role", nullable = false)
	private UserType userRole;

	@Column(name = "introduction", length = 200)
	private String introduction;

	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
	private List<FavoriteCenter> favoriteCenters = new ArrayList<>();

	@Builder
	private Member(
		String email,
		String nickname,
		String password,
		String address,
		String contact,
		String profileImageUrl
	) {
		validateInfo(email, password, nickname, contact, address);

		this.email = email;
		this.password = password;
		this.nickname = nickname;
		this.contact = contact;
		this.address = address;
		this.profileImageUrl = profileImageUrl;
		this.userRole = UserType.MEMBER;
	}

	private void validateInfo(
		String email,
		String password,
		String nickname,
		String contact,
		String address
	) {
		Assert.hasText(email, "Updated email cannot be null or blank.");
		Assert.hasText(password, "Updated hashed password cannot be null or blank.");
		Assert.hasText(nickname, "Updated center nickName cannot be null or blank.");
		Assert.hasText(contact, "Updated contact cannot be null or blank.");
		Assert.hasText(address, "Updated address cannot be null or blank.");
	}

	public void changeMemberInfo(
		String email,
		String password,
		String nickname,
		String contact,
		String address,
		String profileImageUrl,
		String introduction
	) {
		validateInfo(email, password, nickname, contact, address);

		this.password = password;
		this.nickname = nickname;
		this.contact = contact;
		this.address = address;
		this.profileImageUrl = profileImageUrl;
		this.introduction = introduction;
	}

	public void addFavCenter(Center center) {
		this.favoriteCenters.add(buildFavCenter(center));
	}

	public void deleteFavCenter(Center center) {
		List<FavoriteCenter> newFavCenters = this.favoriteCenters.stream()
																 .filter(
																	 favCenter -> !favCenter
																		 .getCenter()
																		 .equals(center))
																 .collect(Collectors.toList());
		this.favoriteCenters = newFavCenters;
	}

	public FavoriteCenter buildFavCenter(Center center) {
		return FavoriteCenter.createFavCenter(this, center);
	}

}
