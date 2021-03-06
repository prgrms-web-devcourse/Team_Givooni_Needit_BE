package com.prgrms.needit.common.enums;

import com.prgrms.needit.common.error.ErrorCode;
import com.prgrms.needit.common.error.exception.InvalidArgumentException;
import java.util.Arrays;
import lombok.Getter;

@Getter
public enum DonationStatus {
	DONATING("기부진행"),
	DONE("기부종료");

	public final String type;

	DonationStatus(String type) {
		this.type = type;
	}

	public static DonationStatus of(String status) {
		return Arrays.stream(DonationStatus.values())
					 .filter(
						 donationStatus -> donationStatus.type.equalsIgnoreCase(status)
					 )
					 .findFirst()
					 .orElseThrow(
						 () -> new InvalidArgumentException(ErrorCode.INVALID_STATUS_VALUE)
					 );
	}
}
