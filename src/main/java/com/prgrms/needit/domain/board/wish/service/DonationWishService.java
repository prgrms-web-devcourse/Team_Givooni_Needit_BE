package com.prgrms.needit.domain.board.wish.service;

import com.prgrms.needit.domain.board.wish.repository.DonationWishRepository;
import org.springframework.stereotype.Service;

@Service
public class DonationWishService {

	private final DonationWishRepository donationWishRepository;

	public DonationWishService(DonationWishRepository donationWishRepository) {
		this.donationWishRepository = donationWishRepository;
	}
}
