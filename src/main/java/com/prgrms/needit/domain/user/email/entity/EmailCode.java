package com.prgrms.needit.domain.user.email.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "email_code")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailCode {

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "email", nullable = false)
	private String email;

	@Column(name = "email_code", nullable = false)
	private String emailCode;

	@Builder
	private EmailCode(Long id, String email, String emailCode) {
		this.id = id;
		this.email = email;
		this.emailCode = emailCode;
	}

	public void changeEmailCode(String emailCode) {
		this.emailCode = emailCode;
	}

}
