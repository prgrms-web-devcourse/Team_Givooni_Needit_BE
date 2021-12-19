package com.prgrms.needit.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOrigins(
					"http://localhost:3000",
					"http://127.0.0.1:3000",
					"https://zen-stonebraker-5ee680.netlify.app"
				)
				.allowedHeaders("*")
				.allowCredentials(true);
	}
}
