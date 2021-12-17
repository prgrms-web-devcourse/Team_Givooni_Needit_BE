package com.prgrms.needit.domain.user.openAPI.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BusinessRawResponse {

	@JsonProperty("data")
	private List<Data> data;

	@JsonProperty("valid_cnt")
	private int valid_cnt;

	@JsonProperty("request_cnt")
	private int request_cnt;

	@JsonProperty("status_code")
	private String status_code;

	@Getter
	public static class Data {

		@JsonProperty("status")
		private Status status;
		@JsonProperty("request_param")
		private Request_param request_param;
		@JsonProperty("valid_msg")
		private String valid_msg;
		@JsonProperty("valid")
		private String valid;
		@JsonProperty("b_no")
		private String b_no;

	}

	@Getter
	public static class Status {

		@JsonProperty("invoice_apply_dt")
		private String invoice_apply_dt;
		@JsonProperty("tax_type_change_dt")
		private String tax_type_change_dt;
		@JsonProperty("utcc_yn")
		private String utcc_yn;
		@JsonProperty("end_dt")
		private String end_dt;
		@JsonProperty("tax_type_cd")
		private String tax_type_cd;
		@JsonProperty("tax_type")
		private String tax_type;
		@JsonProperty("b_stt_cd")
		private String b_stt_cd;
		@JsonProperty("b_stt")
		private String b_stt;
		@JsonProperty("b_no")
		private String b_no;

	}

	@Getter
	public static class Request_param {

		@JsonProperty("b_type")
		private String b_type;
		@JsonProperty("b_sector")
		private String b_sector;
		@JsonProperty("corp_no")
		private String corp_no;
		@JsonProperty("b_nm")
		private String b_nm;
		@JsonProperty("p_nm2")
		private String p_nm2;
		@JsonProperty("p_nm")
		private String p_nm;
		@JsonProperty("start_dt")
		private String start_dt;
		@JsonProperty("b_no")
		private String b_no;

	}
}
