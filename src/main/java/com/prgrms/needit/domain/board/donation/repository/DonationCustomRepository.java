package com.prgrms.needit.domain.board.donation.repository;

import com.prgrms.needit.domain.board.donation.entity.Donation;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DonationCustomRepository {

	Page<Donation> searchAllByFilter(
		String title,
		String category,
		List<Long> tags,
		Pageable pageable
	);
}
