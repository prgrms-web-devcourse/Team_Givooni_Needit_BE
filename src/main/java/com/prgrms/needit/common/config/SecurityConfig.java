package com.prgrms.needit.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private static final String[] AUTH_WHITELIST = {
		"/swagger-resources/**",
		"/swagger-ui.html",
		"/v2/api-docs",
		"/webjars/**",
		"/stomp-handshake"
	};

	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	public void configure(WebSecurity web) {
		web.ignoring()
		   .antMatchers(AUTH_WHITELIST);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf()
			.disable();
	}

}
