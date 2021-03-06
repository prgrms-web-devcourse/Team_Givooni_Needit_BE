package com.prgrms.needit.common.enums;

import lombok.Getter;

@Getter
public enum UserType {
	MEMBER("ROLE_MEMBER", "νμ"),
	CENTER("ROLE_CENTER", "μΌν°");

	private String key;
	private String type;

	UserType(String key, String type) {
		this.key = key;
		this.type = type;
	}

}