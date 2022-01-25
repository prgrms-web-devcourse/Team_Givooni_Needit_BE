package com.prgrms.needit.domain.user.center.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.prgrms.needit.common.BaseIntegrationTest;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

class CenterControllerTest extends BaseIntegrationTest {

	private final String testEmail = "TEST-CENTER1@email.com";
	private final String testPW = "TEST-CENTER1";
	private final String testName = "TEST-CENTER1";
	private final String testContact = "02-123-1234";
	private final String testAddress = "TEST-CENTER1@email.com";
	private final String testOwner = "TEST-CENTER1@email.com";
	private final String testREG = "123456";
	private final String testIntro = "this is test-center introduction.";
	@Autowired
	CenterService centerService;
	private Long testCenterId;

	@BeforeEach
	void init() {
		testCenterId = centerService.createCenter(
			CenterCreateRequest.builder()
							   .email(testEmail)
							   .password(testPW)
							   .name(testName)
							   .contact(testContact)
							   .address(testAddress)
							   .owner(testOwner)
							   .registrationCode(testREG)
							   .build());
	}

	@Test
	@DisplayName("센터 가입")
	void createCenter() throws Exception {
		CenterCreateRequest request = CenterCreateRequest.builder()
														 .email("TEST-CENTER2@email.com")
														 .password("TEST-CENTER2")
														 .name("TEST-CENTER2")
														 .contact("02-798-4651")
														 .address("서울특별시 도봉구")
														 .owner("TESTER2")
														 .registrationCode("123123123")
														 .build();

		this.mockMvc
			.perform(MockMvcRequestBuilders
						 .post("/centers/signup")
						 .content(objectMapper.writeValueAsString(request))
						 .contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.message").value("success"))
			.andExpect(jsonPath("$.data").exists());
	}

	@Test
	@DisplayName("센터 이름 검색")
	void searchCenter() throws Exception {
		MultiValueMap<String, String> requestParam = new LinkedMultiValueMap<>();
		requestParam.set("centerName", testName);

		this.mockMvc
			.perform(MockMvcRequestBuilders
						 .get("/centers/search")
						 .params(requestParam)
						 .contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.message").value("success"))
			.andExpect(jsonPath("$.data[0].centerId").value(testCenterId))
			.andExpect(jsonPath("$.data[0].name").value(testName))
			.andExpect(jsonPath("$.data[0].contact").value(testContact))
			.andExpect(jsonPath("$.data[0].address").value(testAddress))
			.andExpect(jsonPath("$.data[0].owner").value(testOwner));
	}

	@Test
	@DisplayName("타 센터 조회")
	void getOtherCenter() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders
						 .get("/centers/{id}", testCenterId)
						 .contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.message").value("success"))
			.andExpect(jsonPath("$.data.myProfile.id").value(testCenterId))
			.andExpect(jsonPath("$.data.myProfile.email").value(testEmail))
			.andExpect(jsonPath("$.data.myProfile.name").value(testName))
			.andExpect(jsonPath("$.data.myProfile.contact").value(testContact))
			.andExpect(jsonPath("$.data.myProfile.address").value(testAddress))
			.andExpect(jsonPath("$.data.myProfile.owner").value(testOwner));
	}

	@Test
	@DisplayName("센터 정보 수정")
	@WithUserDetails(value = "member@email.com")
	void updateCenter() throws Exception {
		String updatedPW = "UPDATED-CENTER";
		String updatedName = "UPDATED-NAME";
		String updatedContact = "02-794-8543";
		String updatedAddress = "서울특별시 강남구";
		String updatedOwner = "UPDATED-OWNER";

		MockMultipartFile image = new MockMultipartFile(
			"file", "image-file.jpeg",
			"image/jpeg", "<<jpeg data>>".getBytes()
		);

		CenterUpdateRequest updateRequest = CenterUpdateRequest.builder()
															   .password(updatedPW)
															   .name(updatedName)
															   .contact(updatedContact)
															   .address(updatedAddress)
															   .owner(updatedOwner)
															   .introduction(testIntro)
															   .build();

		String content = objectMapper.writeValueAsString(updateRequest);
		MockMultipartFile json = new MockMultipartFile(
			"request", "json-data",
			"application/json", content.getBytes(StandardCharsets.UTF_8)
		);

		MockMultipartHttpServletRequestBuilder builder =
			MockMvcRequestBuilders.multipart("/centers");
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
			.andExpect(jsonPath("$.data").value(testCenterId));
	}

}