package com.prgrms.needit.domain.user.openAPI.service;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.needit.common.error.ErrorCode;
import com.prgrms.needit.common.error.exception.InvalidArgumentException;
import com.prgrms.needit.common.error.exception.OpenApiException;
import com.prgrms.needit.domain.user.openAPI.dto.BusinessRawResponse;
import com.prgrms.needit.domain.user.openAPI.dto.BusinessRefinedResponse;
import com.prgrms.needit.domain.user.openAPI.dto.BusinessRequest;
import com.prgrms.needit.domain.user.openAPI.dto.BusinessesRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@PropertySource("classpath:application.yml")
public class OpenApiService {

	@Value("${open-api.base-url}")
	private String baseUrl;

	@Value("${open-api.service-key}")
	private String serviceKey;

	public BusinessRefinedResponse checkBusinesscode(BusinessRequest request) {

		final String urlStr = baseUrl + "serviceKey=" + serviceKey;

		BusinessesRequest listRequest = new BusinessesRequest(request);

		String listRequestStr = parseToString(listRequest);

		try {
			URL url = new URL(urlStr);

			HttpURLConnection con = setConnection(url);

			OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
			wr.write(listRequestStr);
			wr.flush();

			return parseToResponse(receiveApiResponse(con));

		} catch (IllegalStateException e) {
			log.info("Error: {}", e.getMessage());
			throw new OpenApiException(ErrorCode.ALREADY_EXIST_OPENAPI_CONN);
		} catch (UnsupportedEncodingException e) {
			log.info("Error: {}", e.getMessage());
			throw new OpenApiException(ErrorCode.UNSUPPORTED_ENCODING);
		} catch (IOException e) {
			log.info("Error: {}", e.getMessage());
			throw new OpenApiException(ErrorCode.OPENAPI_CONN_FAIL);
		}
	}

	private HttpURLConnection setConnection(URL url) {
		try {
			HttpURLConnection con = (HttpURLConnection) url.openConnection();

			con.setRequestMethod("POST");
			con.setRequestProperty("accept", "application/json");
			con.setRequestProperty("Content-Type", "application/json");
			con.setDoInput(true);
			con.setDoOutput(true);

			return con;

		} catch (IOException e) {
			throw new OpenApiException(ErrorCode.OPENAPI_CONN_FAIL);
		}
	}

	private String receiveApiResponse(HttpURLConnection con) {
		try {
			if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
				StringBuilder sb = new StringBuilder();
				BufferedReader br = new BufferedReader(
					new InputStreamReader(con.getInputStream(), "utf-8"));
				String line;
				while ((line = br.readLine()) != null) {
					sb.append(line)
					  .append("\n");
				}
				br.close();
				con.disconnect();

				String result = sb.toString();
				return result;
			}
			throw new OpenApiException(ErrorCode.OPENAPI_CONN_FAIL);
		} catch (IOException e) {
			log.info("Error: {}", e.getMessage());
			throw new OpenApiException(ErrorCode.INVALID_INPUT_VALUE);
		}
	}

	private String parseToString(BusinessesRequest listRequest) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(listRequest);
		} catch (JsonProcessingException e) {
			log.info("Error: {}", e.getMessage());
			throw new InvalidArgumentException(ErrorCode.INVALID_INPUT_VALUE);
		}
	}

	private BusinessRefinedResponse parseToResponse(String string) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			BusinessRawResponse response = mapper.readValue(string, BusinessRawResponse.class);
			return new BusinessRefinedResponse(response);
		} catch (JacksonException e) {
			log.info("Error: {}", e.getMessage());
			throw new InvalidArgumentException(ErrorCode.INVALID_INPUT_VALUE);
		}
	}

}
