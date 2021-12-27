package com.prgrms.needit.domain.user.favorite.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.prgrms.needit.common.BaseIntegrationTest;
import com.prgrms.needit.domain.user.favorite.service.FavoriteCenterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@ActiveProfiles(profiles = "dev")
class FavoriteCenterControllerTest extends BaseIntegrationTest {

	@Autowired
	private FavoriteCenterService favCenterService;

	private final Long centerId = 5L;

	@BeforeEach
	@WithUserDetails(value = "member@email.com")
	void init() {
		Long favCenterId = favCenterService.createFavoriteCenter(centerId);
	}

	@Test
	@WithUserDetails(value = "member@email.com")
	void getFavCenters() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders
						 .get("/favorites"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.message").value("success"))
			.andExpect(jsonPath("$.data[0].centerId").value(centerId));
	}

	@Test
	@WithUserDetails(value = "member@email.com")
	void addFavoriteCenter() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders
						 .post("/favorites/{centerId}", 1L))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.message").value("success"))
			.andExpect(jsonPath("$.data").exists());
	}

}