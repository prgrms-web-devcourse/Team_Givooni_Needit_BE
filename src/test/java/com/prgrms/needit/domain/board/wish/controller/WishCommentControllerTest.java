package com.prgrms.needit.domain.board.wish.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.prgrms.needit.common.BaseIntegrationTest;
import com.prgrms.needit.common.domain.dto.CommentRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public class WishCommentControllerTest extends BaseIntegrationTest {

	private static final Long DONATION_WISH_ID = 1L;
	private static final Long WISH_COMMENT_ID = 1L;
	private static final String COMMENT = "기부신청";

	@DisplayName("회원의 기부신청댓글 등록")
	@WithUserDetails(value = "member@email.com")
	@Test
	void registerComment() throws Exception {
		CommentRequest registerRequest = new CommentRequest(COMMENT);

		this.mockMvc
			.perform(MockMvcRequestBuilders
						 .post("/wishes/{id}/comments", DONATION_WISH_ID)
						 .content(objectMapper.writeValueAsString(registerRequest))
						 .contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.message").value("success"))
			.andExpect(jsonPath("$.data").exists());
	}

	@DisplayName("회원의 기부신청댓글 삭제")
	@WithUserDetails(value = "member@email.com")
	@Test
	void removeComment() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders
						 .delete(
							 "/wishes/{wishId}/comments/{commentId}",
							 DONATION_WISH_ID, WISH_COMMENT_ID
						 )
			)
			.andExpect(status().isNoContent());
	}

	@DisplayName("회원이 아닌 센터가 기부신청댓글에 접근할 때 예외처리")
	@WithUserDetails(value = "center@email.com")
	@Test
	void removeCommentByCenter() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders
						 .delete(
							 "/wishes/{wishId}/comments/{commentId}",
							 DONATION_WISH_ID, WISH_COMMENT_ID
						 )
			)
			.andExpect(status().is4xxClientError());
	}

}
