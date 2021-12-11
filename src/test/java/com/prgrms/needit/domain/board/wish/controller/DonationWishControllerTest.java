package com.prgrms.needit.domain.board.wish.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.prgrms.needit.common.BaseIntegrationTest;
import com.prgrms.needit.common.domain.dto.DealStatusRequest;
import com.prgrms.needit.domain.board.wish.dto.DonationWishRequest;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

class DonationWishControllerTest extends BaseIntegrationTest {

	private static final Long ID = 1L;
	private static final String TITLE = "기부";
	private static final String CONTENT = "기부원해요";
	private static final String CATEGORY = "물품나눔";
	private static final List<Long> TAGS = new ArrayList<>(List.of(1L, 2L, 3L));
	private static final String UPDATE_STATUS = "기부종료";

	@DisplayName("센터의 기부희망글 등록")
	@WithUserDetails(value = "center@email.com")
	@Test
	void registerDonationWish() throws Exception {
		DonationWishRequest registerRequest = new DonationWishRequest(
			TITLE, CONTENT, CATEGORY, TAGS
		);

		this.mockMvc
			.perform(MockMvcRequestBuilders
						 .post("/wishes")
						 .content(objectMapper.writeValueAsString(registerRequest))
						 .contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.message").value("success"))
			.andExpect(jsonPath("$.data").exists());
	}

	@DisplayName("센터의 기부희망글 정보수정")
	@WithUserDetails(value = "center@email.com")
	@Test
	void modifyDonationWish() throws Exception {
		DonationWishRequest modifyRequest = new DonationWishRequest(
			TITLE, CONTENT, CATEGORY, TAGS
		);

		this.mockMvc
			.perform(MockMvcRequestBuilders
						 .put("/wishes/{id}", ID)
						 .content(objectMapper.writeValueAsString(modifyRequest))
						 .contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.message").value("success"))
			.andExpect(jsonPath("$.data").value(ID));
	}

	@DisplayName("센터의 기부희망글 거래상태 변경")
	@WithUserDetails(value = "center@email.com")
	@Test
	void modifyDealStatus() throws Exception {
		DealStatusRequest modifyStatusRequest = new DealStatusRequest(UPDATE_STATUS);

		this.mockMvc
			.perform(MockMvcRequestBuilders
						 .patch("/wishes/{id}", ID)
						 .content(objectMapper.writeValueAsString(modifyStatusRequest))
						 .contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.message").value("success"))
			.andExpect(jsonPath("$.data").value(ID));
	}

	@DisplayName("센터의 기부희망글 삭제")
	@WithUserDetails(value = "center@email.com")
	@Test
	void removeDonationWish() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders
						 .delete("/wishes/{id}", ID))
			.andExpect(status().isNoContent());
	}

	@DisplayName("센터가 아닌 회원이 기부희망글에 접근할 때 예외처리")
	@WithUserDetails(value = "member@email.com")
	@Test
	void removeDonationWishByMember() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders
						 .delete("/wishes/{id}", ID))
			.andExpect(status().is4xxClientError());
	}
}