package com.prgrms.needit.common.response;

import lombok.Getter;

@Getter
public class ApiResponse<T> {

	private String message;
	private T data;

	private ApiResponse(String message, T data) {
		this.message = message;
		this.data = data;
	}

	public static <T> ApiResponse<T> of(T data) {
		return new ApiResponse<>("success", data);
	}
}
