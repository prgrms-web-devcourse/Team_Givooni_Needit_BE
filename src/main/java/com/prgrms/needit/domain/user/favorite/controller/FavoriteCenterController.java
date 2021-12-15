package com.prgrms.needit.domain.user.favorite.controller;

import com.prgrms.needit.common.response.ApiResponse;
import com.prgrms.needit.domain.user.center.dto.CenterListResponse;
import com.prgrms.needit.domain.user.favorite.service.FavoriteCenterService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

	@GetMapping("/favorites")
	public ResponseEntity<ApiResponse<List<CenterListResponse>>> getFavCenters() {
		return ResponseEntity.ok(
			ApiResponse.of(favCenterService.getFavCenters())
		);
	}

	@PostMapping("centers/{centerId}/favorites")
	public ResponseEntity<ApiResponse<Long>> addFavoriteCenter(
		@PathVariable Long id
	) {
		return ResponseEntity.ok(
			ApiResponse.of(favCenterService.addFavoriteCenter(id))
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
