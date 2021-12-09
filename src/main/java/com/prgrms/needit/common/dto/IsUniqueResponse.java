package com.prgrms.needit.common.dto;

import lombok.Getter;

@Getter
public class IsUniqueResponse {

	private final boolean isUnique;

	public IsUniqueResponse(boolean isUnique) {
		this.isUnique = isUnique;
	}
}
