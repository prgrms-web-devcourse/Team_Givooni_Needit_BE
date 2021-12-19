package com.prgrms.needit.domain.user.center.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BusinessRawResponse {

	private List<Data> data;

	private int valid_cnt;

	private int request_cnt;

	@JsonProperty("status_code")
	private String statusCode;

	@Getter
	public static class Data {

		private Status status;

		private Request_param request_param;

		@JsonProperty("valid_msg")
		private String validMsg;

		private String valid;

		private String b_no;

	}

	@Getter
	public static class Status {

		private String invoice_apply_dt;

		private String tax_type_change_dt;

		private String utcc_yn;

		private String end_dt;

		private String tax_type_cd;

		private String tax_type;

		private String b_stt_cd;

		private String b_stt;

		private String b_no;

	}

	@Getter
	public static class Request_param {


		private String b_type;

		private String b_sector;

		private String corp_no;

		private String b_nm;

		private String p_nm2;

		private String p_nm;

		private String start_dt;

		private String b_no;

	}
}
