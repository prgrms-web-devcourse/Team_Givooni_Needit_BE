package com.prgrms.needit.domain.board.wish.controller;

import com.prgrms.needit.domain.board.wish.service.DonationWishService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wishes")
public class DonationWishController {
	private final DonationWishService donationWishService;

	public DonationWishController(DonationWishService donationWishService) {
		this.donationWishService = donationWishService;
	}
}
