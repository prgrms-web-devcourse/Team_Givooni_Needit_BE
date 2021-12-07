package com.prgrms.needit.domain.board.donation.repository;

import com.prgrms.needit.domain.board.donation.dto.DonationFilterRequest;
import com.prgrms.needit.domain.board.donation.entity.Donation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DonationCustomRepository {

	Page<Donation> searchAllByFilter(
		DonationFilterRequest request,
		Pageable pageable
	);
}
