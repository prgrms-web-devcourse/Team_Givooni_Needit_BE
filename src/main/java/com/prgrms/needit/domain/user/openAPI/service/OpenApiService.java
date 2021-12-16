package com.prgrms.needit.domain.user.openAPI.service;

import com.prgrms.needit.common.error.ErrorCode;
import com.prgrms.needit.common.error.exception.OpenApiException;
import com.prgrms.needit.domain.user.openAPI.dto.BusinessInfoResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OpenApiService {

	private final String baseUrl = "https://api.odcloud.kr/api/nts-businessman/v1/validate?";
	private final String serviceKey = "ALn9apv%2Fo2QGOA6ImPROWYHDh6H%2BWeHT5nV5yG7UjW3hkwOwA41g2WBFQSVxHkfjTQL1HGrHrsWyoCmodo4Y5A%3D%3D";
	private final String urlStr = baseUrl + "serviceKey=" + serviceKey;

	private final String jsonInputString = "{\"businesses\": [{\"b_no\": \"8178801986\",\"start_dt\": \"20210401\",\"p_nm\": \"김민주\"}]}";

	public String callApi() {
		try {
			URL url = new URL(urlStr);

			HttpURLConnection con = setConnection(url);

			OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
			wr.write(jsonInputString); //json 형식의 message 전달
			wr.flush();

			return receiveApiResponse(con);

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
			con.setDoOutput(true); //POST 데이터를 OutputStream으로 넘겨 주겠다는 설정

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
				System.out.println(result);
				return result;
			}
			throw new OpenApiException(ErrorCode.OPENAPI_CONN_FAIL);
		} catch (IOException e) {
			log.info("Error: {}", e.getMessage());
			throw new OpenApiException(ErrorCode.OPENAPI_CONN_FAIL);
		}
	}

	private BusinessInfoResponse toResponse(String string) {
		return null;
	}

}
