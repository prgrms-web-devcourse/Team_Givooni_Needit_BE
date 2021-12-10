package com.prgrms.needit.common.enums;

import lombok.Getter;

@Getter
public enum UserType {
	MEMBER("ROLE_MEMBER", "회원"),
	CENTER("ROLE_CENTER", "센터");

	private String key;
	private String type;

	UserType(String key, String type) {
		this.key = key;
		this.type = type;
	}
  
}
