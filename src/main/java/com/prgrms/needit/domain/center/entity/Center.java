package com.prgrms.needit.domain.center.entity;

import com.prgrms.needit.common.domain.BaseEntity;
import com.prgrms.needit.common.domain.enums.DonationCategory;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

@Getter
@Entity
@Table(name = "center")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Center extends BaseEntity {

	@Column(name = "email", nullable = false, unique = true)
	private String email;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "name", length = 128, nullable = false, unique = true)
	private String name;

	@Column(name = "contact", length = 16, nullable = false, unique = true)
	private String contact;

	@Column(name = "address", length = 128, nullable = false, unique = true)
	private String address;

	@Column(name = "image", length = 512, nullable = false)
	private String profileImageUrl;

	@Column(name = "owner", length = 32, nullable = false)
	private String owner;

	@Column(name = "registration_code", length = 16, nullable = false, unique = true)
	private String registrationCode;

	@Builder
	private Center(
		String email,
		String password,
		String name,
		String contact,
		String address,
		String profileImageUrl,
		String owner,
		String registrationCode
	) {
		validateInfo(email, password, name, contact, address, owner, registrationCode);

		this.email = email;
		this.password = password;
		this.name = name;
		this.contact = contact;
		this.address = address;
		this.profileImageUrl = profileImageUrl;
		this.owner = owner;
		this.registrationCode = registrationCode;
	}

	private void validateInfo(String email,
							  String password,
							  String name,
							  String contact,
							  String address,
							  String owner,
							  String registrationCode) {
		Assert.hasText(email, "Updated email cannot be null or blank.");
		Assert.hasText(password, "Updated hashed password cannot be null or blank.");
		Assert.hasText(name, "Updated center name cannot be null or blank.");
		Assert.hasText(contact, "Updated contact cannot be null or blank.");
		Assert.hasText(address, "Updated address cannot be null or blank.");
		Assert.hasText(owner, "Updated owner cannot be null or blank.");
		Assert.hasText(registrationCode, "Updated registration code cannot be null or blank.");
	}

	public void changeCenterInfo(
		String email,
		String password,
		String name,
		String contact,
		String address,
		String profileImageUrl,
		String owner,
		String registrationCode
	) {
		validateInfo(email, password, name, contact, address, owner, registrationCode);

		this.email = email;
		this.password = password;
		this.name = name;
		this.contact = contact;
		this.address = address;
		this.profileImageUrl = profileImageUrl;
		this.owner = owner;
		this.registrationCode = registrationCode;
	}
}
