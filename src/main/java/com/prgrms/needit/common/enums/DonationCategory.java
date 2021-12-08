package com.prgrms.needit.common.enums;

import com.prgrms.needit.common.error.ErrorCode;
import com.prgrms.needit.common.error.exception.InvalidArgumentException;
import java.util.Arrays;
import lombok.Getter;

@Getter
public enum DonationCategory {
	GOODS("물품"),
	TALENT("재능");

	public final String type;

	DonationCategory(String type) {
		this.type = type;
	}

	public static DonationCategory of(String category) {
		return Arrays.stream(DonationCategory.values())
					 .filter(
						 donationCategory -> donationCategory.type.equalsIgnoreCase(category)
					 )
					 .findFirst()
					 .orElseThrow(
						 () -> new InvalidArgumentException(ErrorCode.INVALID_CATEGORY_VALUE)
					 );
	}
}
