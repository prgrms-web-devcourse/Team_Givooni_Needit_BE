package com.prgrms.needit.common.domain;

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
@Table(name = "theme_tag")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ThemeTag {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name", length = 32, nullable = false, unique = true)
	private String tagName;

	private ThemeTag(String tagName) {
		this.tagName = tagName;
	}

	public static ThemeTag registerTag(String tagName) {
		return new ThemeTag(tagName);
	}

}
