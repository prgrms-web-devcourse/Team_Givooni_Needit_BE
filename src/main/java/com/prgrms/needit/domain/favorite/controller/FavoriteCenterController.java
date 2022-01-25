package com.prgrms.needit.domain.favorite.controller;

import com.prgrms.needit.common.response.ApiResponse;
import com.prgrms.needit.domain.favorite.service.FavoriteCenterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FavoriteCenterController {

	private final FavoriteCenterService favCenterService;

	public FavoriteCenterController(
		FavoriteCenterService favCenterService
	) {
		this.favCenterService = favCenterService;
	}

	@PostMapping("/favorites/{centerId}")
	public ResponseEntity<ApiResponse<Long>> addFavoriteCenter(
		@PathVariable Long centerId
	) {
		return ResponseEntity.ok(
			ApiResponse.of(favCenterService.addFavoriteCenter(centerId))
		);
	}

	@DeleteMapping("/favorites/{centerId}")
	public ResponseEntity<ApiResponse<Void>> removeFavoriteCenter(
		@PathVariable Long centerId
	) {
		favCenterService.removeFavoriteCenter(centerId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
