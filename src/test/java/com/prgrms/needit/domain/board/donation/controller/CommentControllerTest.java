package com.prgrms.needit.domain.board.donation.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.prgrms.needit.common.BaseIntegrationTest;
import com.prgrms.needit.common.domain.dto.CommentRequest;
import com.prgrms.needit.common.error.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public class CommentControllerTest extends BaseIntegrationTest {

	private static final Long DONATION_ID = 1L;
	private static final Long COMMENT_ID = 1L;
	private static final String COMMENT = "기부희망";
	private static final Long NO_ID = 0L;

	@DisplayName("센터의 기부희망댓글 등록")
	@Test
	void registerComment() throws Exception {
		CommentRequest registerRequest = new CommentRequest(COMMENT);

		this.mockMvc
			.perform(MockMvcRequestBuilders
						 .post("/donations/{id}/comments", DONATION_ID)
						 .content(objectMapper.writeValueAsString(registerRequest))
						 .contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.message").value("success"))
			.andExpect(jsonPath("$.data").exists());
	}

	@DisplayName("센터의 기부희망댓글 삭제")
	@Test
	void removeComment() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders
						 .delete(
							 "/donations/{donationId}/comments/{commentId}",
							 DONATION_ID, COMMENT_ID
						 )
			)
			.andExpect(status().isNoContent());
	}

	@DisplayName("기부희망댓글이 존재하지 않을 경우 예외처리")
	@Test
	void removeDonationByNoDonation() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders
						 .delete(
							 "/donations/{donationId}/comments/{commentId}",
							 DONATION_ID, NO_ID
						 )
			)
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.message")
						   .value(ErrorCode.NOT_FOUND_WISH_COMMENT.getMessage()))
			.andExpect(jsonPath("$.code")
						   .value(ErrorCode.NOT_FOUND_WISH_COMMENT.getCode()));
	}

}
