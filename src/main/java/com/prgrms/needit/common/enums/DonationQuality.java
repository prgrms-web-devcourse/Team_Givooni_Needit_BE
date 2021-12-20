package com.prgrms.needit.common.enums;

import com.prgrms.needit.common.error.ErrorCode;
import com.prgrms.needit.common.error.exception.InvalidArgumentException;
import java.util.Arrays;
import lombok.Getter;

@Getter
public enum DonationQuality {
	HIGH("좋음"),
	MEDIUM("보통"),
	LOW("나쁨");

	public final String type;

	DonationQuality(String type) {
		this.type = type;
	}

	public static DonationQuality of(String quality) {
		return Arrays.stream(DonationQuality.values())
					 .filter(
						 donationQuality -> donationQuality.type.equalsIgnoreCase(quality)
					 )
					 .findFirst()
					 .orElseThrow(
						 () -> new InvalidArgumentException(ErrorCode.INVALID_QUALITY_VALUE)
					 );
	}
}
