package com.prgrms.needit.domain.board.wish.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.prgrms.needit.common.BaseIntegrationTest;
import com.prgrms.needit.common.domain.dto.DealStatusRequest;
import com.prgrms.needit.domain.board.wish.dto.DonationWishRequest;
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

class DonationWishControllerTest extends BaseIntegrationTest {

	private static final Long ID = 1L;
	private static final String TITLE = "기부";
	private static final String CONTENT = "기부원해요";
	private static final String CATEGORY = "물품나눔";
	private static final List<Long> TAGS = new ArrayList<>(List.of(1L, 2L, 3L));
	private static final String UPDATE_STATUS = "기부종료";

	@DisplayName("기부희망글 목록 조회")
	@Test
	void getDonationWishes() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders
						 .get("/wishes/search", ID)
						 .param("page", "1")
						 .param("size", "5")
						 .param("category", "재능기부")
						 .param("centerName", "테스트"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.message").value("success"))
			.andExpect(jsonPath("$.data.content").isArray())
			.andExpect(jsonPath("$.data.content[0].id").isNumber())
			.andExpect(jsonPath("$.data.content[0].title").isString())
			.andExpect(jsonPath("$.data.content[0].content").isString())
			.andExpect(jsonPath("$.data.content[0].category").isString())
			.andExpect(jsonPath("$.data.content[0].status").isString())
			.andExpect(jsonPath("$.data.content[0].userId").isNumber())
			.andExpect(jsonPath("$.data.content[0].userName").isString())
			.andExpect(jsonPath("$.data.content[0].userImage").isString())
			.andExpect(jsonPath("$.data.content[0].userCnt").isNumber())
			.andExpect(jsonPath("$.data.content[0].tags").isArray())
			.andExpect(jsonPath("$.data.content[0].images").isArray())
			.andExpect(jsonPath("$.data.content[0].comments").isArray());
	}

	@DisplayName("기부희망글 상세 조회")
	@Test
	void getDonationWish() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.get("/wishes/{id}", ID))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.message").value("success"))
			.andExpect(jsonPath("$.data.id").isNumber())
			.andExpect(jsonPath("$.data.title").isString())
			.andExpect(jsonPath("$.data.content").isString())
			.andExpect(jsonPath("$.data.category").isString())
			.andExpect(jsonPath("$.data.status").isString())
			.andExpect(jsonPath("$.data.userId").isNumber())
			.andExpect(jsonPath("$.data.userName").isString())
			.andExpect(jsonPath("$.data.userImage").isString())
			.andExpect(jsonPath("$.data.userCnt").isNumber())
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

	@DisplayName("센터의 기부희망글 등록")
	@WithUserDetails(value = "center@email.com")
	@Test
	void registerDonationWish() throws Exception {
		MockMultipartFile image = new MockMultipartFile(
			"file", "image-file.jpeg",
			"image/jpeg", "<<jpeg data>>".getBytes()
		);

		DonationWishRequest registerRequest = new DonationWishRequest(
			TITLE, CONTENT, CATEGORY, TAGS
		);

		String content = objectMapper.writeValueAsString(registerRequest);
		MockMultipartFile json = new MockMultipartFile(
			"request", "json-data",
			"application/json", content.getBytes(StandardCharsets.UTF_8)
		);

		this.mockMvc
			.perform(MockMvcRequestBuilders
						 .multipart("/wishes")
						 .file(json)
						 .file(image)
						 .contentType("multipart/form-data")
						 .accept(MediaType.APPLICATION_JSON)
						 .characterEncoding("UTF-8"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.message").value("success"))
			.andExpect(jsonPath("$.data").exists());
	}

	@DisplayName("센터의 기부희망글 정보수정")
	@WithUserDetails(value = "center@email.com")
	@Test
	void modifyDonationWish() throws Exception {
		MockMultipartFile image = new MockMultipartFile(
			"file", "image-file.jpeg",
			"image/jpeg", "<<jpeg data>>".getBytes()
		);

		DonationWishRequest modifyRequest = new DonationWishRequest(
			TITLE, CONTENT, CATEGORY, TAGS
		);

		String content = objectMapper.writeValueAsString(modifyRequest);
		MockMultipartFile json = new MockMultipartFile(
			"request", "json-data",
			"application/json", content.getBytes(StandardCharsets.UTF_8)
		);

		MockMultipartHttpServletRequestBuilder builder =
			MockMvcRequestBuilders.multipart("/wishes/{id}", ID);
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