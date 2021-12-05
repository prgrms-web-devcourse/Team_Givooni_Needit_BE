package com.prgrms.needit.domain.board.donation.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.prgrms.needit.common.BaseIntegrationTest;
import com.prgrms.needit.common.error.ErrorCode;
import com.prgrms.needit.domain.board.donation.dto.DonationRequest;
import com.prgrms.needit.domain.board.donation.dto.DonationStatusRequest;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

class DonationControllerTest extends BaseIntegrationTest {

	private static final Long ID = 1L;
	private static final String TITLE = "기부";
	private static final String CONTENT = "기부할래요";
	private static final String CATEGORY = "물품";
	private static final String QUALITY = "상";
	private static final List<Long> TAGS = new ArrayList<>(List.of(1L, 2L, 3L));
	private static final String UPDATE_STATUS = "기부 완료";
	private static final Long NO_ID = 0L;

	@DisplayName("회원의 기부글 등록")
	@Test
	void registerDonation() throws Exception {
		DonationRequest registerRequest = new DonationRequest(
			TITLE, CONTENT, CATEGORY, QUALITY, TAGS
		);

		this.mockMvc
			.perform(MockMvcRequestBuilders
						 .post("/donation")
						 .content(objectMapper.writeValueAsString(registerRequest))
						 .contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.message").value("success"))
			.andExpect(jsonPath("$.data").exists());
	}

	@DisplayName("회원의 기부글 정보수정")
	@Test
	void modifyDonation() throws Exception {
		DonationRequest modifyRequest = new DonationRequest(
			TITLE, CONTENT, CATEGORY, QUALITY, TAGS
		);

		this.mockMvc
			.perform(MockMvcRequestBuilders
						 .put("/donation/{id}", ID)
						 .content(objectMapper.writeValueAsString(modifyRequest))
						 .contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.message").value("success"))
			.andExpect(jsonPath("$.data").value(ID));
	}

	@DisplayName("회원의 기부글 거래상태 변경")
	@Test
	void modifyDealStatus() throws Exception {
		DonationStatusRequest modifyStatusRequest = new DonationStatusRequest(UPDATE_STATUS);

		this.mockMvc
			.perform(MockMvcRequestBuilders
						 .patch("/donation/{id}", ID)
						 .content(objectMapper.writeValueAsString(modifyStatusRequest))
						 .contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.message").value("success"))
			.andExpect(jsonPath("$.data").value(ID));
	}

	@DisplayName("회원의 기부글 삭제")
	@Test
	void removeDonation() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders
						 .delete("/donation/{id}", ID))
			.andExpect(status().isNoContent());
	}

	@DisplayName("기부글이 존재하지 않을 경우 예외처리")
	@Test
	void removeDonationByNoDonation() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders
						 .delete("/donation/{id}", NO_ID))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.message").value(ErrorCode.NOT_FOUND_DONATION.getMessage()))
			.andExpect(jsonPath("$.code").value(ErrorCode.NOT_FOUND_DONATION.getCode()));
	}
}