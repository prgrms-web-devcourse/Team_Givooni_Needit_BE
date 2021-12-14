package com.prgrms.needit.domain.user.login.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class IsUniqueResponse {

	private boolean unique;

	public IsUniqueResponse(boolean unique) {
		this.unique = unique;
	}
}
