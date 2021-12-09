package com.prgrms.needit.domain.member.entity;

import com.prgrms.needit.common.domain.entity.BaseEntity;
import com.prgrms.needit.common.enums.UserType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

@Getter
@Entity
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

	@Column(name = "email", length = 255, nullable = false, unique = true)
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
	private UserType userType;

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
		this.userType = UserType.ROLE_MEMBER;
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
		String profileImageUrl
	) {
		validateInfo(email, password, nickname, contact, address);

		this.email = email;
		this.password = password;
		this.nickname = nickname;
		this.contact = contact;
		this.address = address;
		this.profileImageUrl = profileImageUrl;
	}

}
