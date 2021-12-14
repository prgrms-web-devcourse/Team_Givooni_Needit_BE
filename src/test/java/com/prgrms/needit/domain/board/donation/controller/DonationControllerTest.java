package com.prgrms.needit.domain.board.donation.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.prgrms.needit.common.BaseIntegrationTest;
import com.prgrms.needit.common.domain.dto.DealStatusRequest;
import com.prgrms.needit.common.error.ErrorCode;
import com.prgrms.needit.domain.board.donation.dto.DonationRequest;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

class DonationControllerTest extends BaseIntegrationTest {

	private static final Long ID = 1L;
	private static final String TITLE = "기부";
	private static final String CONTENT = "기부할래요";
	private static final String CATEGORY = "물품나눔";
	private static final String QUALITY = "좋음";
	private static final List<Long> TAGS = new ArrayList<>(List.of(1L, 2L, 3L));
	private static final String UPDATE_STATUS = "기부종료";
	private static final Long NO_ID = 0L;

	@DisplayName("기부글 상세 조회")
	@WithUserDetails(value = "member@email.com")
	@Test
	void getDonation() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.get("/donations/{id}", ID))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.message").value("success"))
			.andExpect(jsonPath("$.data.id").isNumber())
			.andExpect(jsonPath("$.data.title").isString())
			.andExpect(jsonPath("$.data.content").isString())
			.andExpect(jsonPath("$.data.category").isString())
			.andExpect(jsonPath("$.data.quality").isString())
			.andExpect(jsonPath("$.data.status").isString())
			.andExpect(jsonPath("$.data.userId").isNumber())
			.andExpect(jsonPath("$.data.userName").isString())
			.andExpect(jsonPath("$.data.userImage").isString())
			.andExpect(jsonPath("$.data.tags").isArray())
			.andExpect(jsonPath("$.data.tags[0]").isString())
			.andExpect(jsonPath("$.data.images").isArray())
			.andExpect(jsonPath("$.data.comments").isArray())
			.andExpect(jsonPath("$.data.comments[0].id").isNumber())
			.andExpect(jsonPath("$.data.comments[0].comment").isString())
			.andExpect(jsonPath("$.data.comments[0].userId").isNumber())
			.andExpect(jsonPath("$.data.comments[0].userName").isString())
			.andExpect(jsonPath("$.data.comments[0].userImage").isString());
	}

	@DisplayName("회원의 기부글 등록")
	@WithUserDetails(value = "member@email.com")
	@Test
	void registerDonation() throws Exception {
		MockMultipartFile image = new MockMultipartFile(
			"file", "image-file.jpeg",
			"image/jpeg", "<<jpeg data>>".getBytes()
		);

		DonationRequest registerRequest = new DonationRequest(
			TITLE, CONTENT, CATEGORY, QUALITY, TAGS
		);

		String content = objectMapper.writeValueAsString(registerRequest);
		MockMultipartFile json = new MockMultipartFile(
			"request", "json-data",
			"application/json", content.getBytes(StandardCharsets.UTF_8)
		);

		this.mockMvc
			.perform(MockMvcRequestBuilders
						 .multipart("/donations")
						 .file(json)
						 .file(image)
						 .contentType("multipart/form-data")
						 .accept(MediaType.APPLICATION_JSON)
						 .characterEncoding("UTF-8"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.message").value("success"))
			.andExpect(jsonPath("$.data").exists());
	}

	@DisplayName("회원의 기부글 정보수정")
	@WithUserDetails(value = "member@email.com")
	@Test
	void modifyDonation() throws Exception {
		MockMultipartFile image = new MockMultipartFile(
			"file", "image-file.jpeg",
			"image/jpeg", "<<jpeg data>>".getBytes()
		);

		DonationRequest modifyRequest = new DonationRequest(
			TITLE, CONTENT, CATEGORY, QUALITY, TAGS
		);

		String content = objectMapper.writeValueAsString(modifyRequest);
		MockMultipartFile json = new MockMultipartFile(
			"request", "json-data",
			"application/json", content.getBytes(StandardCharsets.UTF_8)
		);

		MockMultipartHttpServletRequestBuilder builder =
			MockMvcRequestBuilders.multipart("/donations/{id}", ID);
		builder.with(
			new RequestPostProcessor() {
				@Override
				public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
					request.setMethod("PUT");
					return request;
				}
			}
		);

		this.mockMvc
			.perform(builder.file(json)
							.file(image)
							.contentType("multipart/form-data")
							.accept(MediaType.APPLICATION_JSON)
							.characterEncoding("UTF-8"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.message").value("success"))
			.andExpect(jsonPath("$.data").value(ID));
	}

	@DisplayName("회원의 기부글 거래상태 변경")
	@WithUserDetails(value = "member@email.com")
	@Test
	void modifyDealStatus() throws Exception {
		DealStatusRequest modifyStatusRequest = new DealStatusRequest(UPDATE_STATUS);

		this.mockMvc
			.perform(MockMvcRequestBuilders
						 .patch("/donations/{id}", ID)
						 .content(objectMapper.writeValueAsString(modifyStatusRequest))
						 .contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.message").value("success"))
			.andExpect(jsonPath("$.data").value(ID));
	}

	@DisplayName("회원의 기부글 삭제")
	@WithUserDetails(value = "member@email.com")
	@Test
	void removeDonation() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders
						 .delete("/donations/{id}", ID))
			.andExpect(status().isNoContent());
	}

	@DisplayName("기부글이 존재하지 않을 경우 예외처리")
	@WithUserDetails(value = "member@email.com")
	@Test
	void removeDonationByNoDonation() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders
						 .delete("/donations/{id}", NO_ID))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.message").value(ErrorCode.NOT_FOUND_DONATION.getMessage()))
			.andExpect(jsonPath("$.code").value(ErrorCode.NOT_FOUND_DONATION.getCode()));
	}
}