package com.prgrms.needit.domain.board.wish.repository;

import com.prgrms.needit.common.domain.dto.DonationFilterRequest;
import com.prgrms.needit.domain.board.wish.entity.DonationWish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WishCustomRepository {

	Page<DonationWish> searchAllByFilter(
		DonationFilterRequest request,
		Pageable pageable
	);

}
