package com.prgrms.needit.domain.user.user.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "center_more_info")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CenterSideInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "owner", length = 32, nullable = false)
	private String owner;

	@Column(name = "registration_code", length = 16, nullable = false, unique = true)
	private String registrationCode;

	private CenterSideInfo(
		String owner,
		String registrationCode
	) {
		this.owner = owner;
		this.registrationCode = registrationCode;
	}

	public static CenterSideInfo addCenterSideInfo(
		String owner,
		String registrationCode
	) {
		return new CenterSideInfo(owner, registrationCode);
	}

	public void changeCenterSideInfo(
		String owner,
		String registrationCode
	) {
		this.owner = owner;
		this.registrationCode = registrationCode;
	}
}
