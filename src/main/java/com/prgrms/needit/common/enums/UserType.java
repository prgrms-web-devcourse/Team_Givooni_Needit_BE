package com.prgrms.needit.common.enums;

import com.prgrms.needit.common.error.ErrorCode;
import com.prgrms.needit.common.error.exception.InvalidArgumentException;
import java.util.Arrays;
import lombok.Getter;

@Getter
public enum UserType {
	MEMBER("ROLE_MEMBER", "member"),
	CENTER("ROLE_CENTER", "center");

	private String key;
	private String type;

	UserType(String key, String type) {
		this.key = key;
		this.type = type;
	}

	public static UserType of(String role) {
		return Arrays.stream(UserType.values())
					 .filter(
						 userRole -> userRole.type.equalsIgnoreCase(role)
					 )
					 .findFirst()
					 .orElseThrow(
						 () -> new InvalidArgumentException(ErrorCode.INVALID_USER_ROLE_VALUE)
					 );
	}

}