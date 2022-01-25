package com.prgrms.needit.domain.member.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.prgrms.needit.common.BaseIntegrationTest;
import com.prgrms.needit.domain.member.dto.MemberCreateRequest;
import com.prgrms.needit.domain.member.dto.MemberUpdateRequest;
import com.prgrms.needit.domain.member.service.MemberService;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

class MemberControllerTest extends BaseIntegrationTest {

	private final String testEmail = "TEST-MEMBER1@email.com";
	private final String testPW = "TEST-MEMBER1";
	private final String testNickname = "TEST-MEMBER1";
	private final String testContact = "02-794-7613";
	private final String testAddress = "TEST-MEMBER1@email.com";
	private final String testIntro = "this is test-member introduction.";
	@Autowired
	MemberService memberService;
	private Long testMemberId;

	@BeforeEach
	void init() {
		testMemberId = memberService.createMember(
			MemberCreateRequest.builder()
							   .email(testEmail)
							   .password(testPW)
							   .nickname(testNickname)
							   .contact(testContact)
							   .address(testAddress)
							   .build());
	}

	@Test
	@DisplayName("멤버 가입")
	void createMember() throws Exception {
		MemberCreateRequest request = MemberCreateRequest.builder()
														 .email("TEST-MEMBER2@email.com")
														 .password("TEST-MEMBER2")
														 .nickname("TEST-MEMBER2")
														 .contact("02-798-4651")
														 .address("서울특별시 도봉구")
														 .build();

		this.mockMvc
			.perform(MockMvcRequestBuilders
						 .post("/members/signup")
						 .content(objectMapper.writeValueAsString(request))
						 .contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.message").value("success"))
			.andExpect(jsonPath("$.data").exists());
	}

	@Test
	@DisplayName("타 멤버 조회")
	void getOtherMember() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders
						 .get("/members/{id}", testMemberId)
						 .contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.message").value("success"))
			.andExpect(jsonPath("$.data.myProfile.id").value(testMemberId))
			.andExpect(jsonPath("$.data.myProfile.email").value(testEmail))
			.andExpect(jsonPath("$.data.myProfile.name").value(testNickname))
			.andExpect(jsonPath("$.data.myProfile.contact").value(testContact))
			.andExpect(jsonPath("$.data.myProfile.address").value(testAddress));
	}

	@Test
	@DisplayName("멤버 정보 수정")
	@WithUserDetails(value = "member@email.com")
	void updateMember() throws Exception {
		String updatedPW = "UPDATED-MEMBER";
		String updatedNickname = "UPDATED-NAME";
		String updatedContact = "02-794-8543";
		String updatedAddress = "서울특별시 강남구";

		MockMultipartFile image = new MockMultipartFile(
			"file", "image-file.jpeg",
			"image/jpeg", "<<jpeg data>>".getBytes()
		);

		MemberUpdateRequest updateRequest = MemberUpdateRequest.builder()
															   .password(updatedPW)
															   .nickname(updatedNickname)
															   .contact(updatedContact)
															   .address(updatedAddress)
															   .introduction(testIntro)
															   .build();

		String content = objectMapper.writeValueAsString(updateRequest);
		MockMultipartFile json = new MockMultipartFile(
			"request", "json-data",
			"application/json", content.getBytes(StandardCharsets.UTF_8)
		);

		MockMultipartHttpServletRequestBuilder builder =
			MockMvcRequestBuilders.multipart("/members");
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
			.perform(builder.file(image)
							.file(json)
							.contentType("multipart/form-data")
							.accept(MediaType.APPLICATION_JSON)
							.characterEncoding("UTF-8"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.message").value("success"))
			.andExpect(jsonPath("$.data").value(testMemberId));
	}

}