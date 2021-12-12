package com.prgrms.needit.domain.board.wish.repository;

import com.prgrms.needit.domain.board.wish.dto.DonationWishFilterRequest;
import com.prgrms.needit.domain.board.wish.entity.DonationWish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WishCustomRepository {

	Page<DonationWish> searchAllByFilter(
		DonationWishFilterRequest request,
		Pageable pageable
	);

}
