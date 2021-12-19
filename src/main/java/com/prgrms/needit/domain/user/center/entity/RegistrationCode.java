package com.prgrms.needit.domain.user.center.entity;

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
@Table(name = "registration_code")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RegistrationCode {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "registration_code", nullable = false)
	private String registrationCode;

	@Column(name = "owner", nullable = false)
	private String owner;

	@Column(name = "start_date")
	private String startDate;

}
