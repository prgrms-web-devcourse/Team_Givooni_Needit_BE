package com.prgrms.needit.common.config;

import com.prgrms.needit.common.config.jwt.JwtAccessDeniedHandler;
import com.prgrms.needit.common.config.jwt.JwtAuthenticationEntryPoint;
import com.prgrms.needit.common.config.jwt.JwtSecurityConfig;
import com.prgrms.needit.common.config.jwt.JwtTokenProvider;
import com.prgrms.needit.common.enums.UserType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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

	private final JwtTokenProvider jwtTokenProvider;
	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

	public SecurityConfig(
		JwtTokenProvider jwtTokenProvider,
		JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
		JwtAccessDeniedHandler jwtAccessDeniedHandler
	) {
		this.jwtTokenProvider = jwtTokenProvider;
		this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
		this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
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
			.disable()

			.exceptionHandling()
			.authenticationEntryPoint(jwtAuthenticationEntryPoint)
			.accessDeniedHandler(jwtAccessDeniedHandler)

			.and()
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)

			.and()
			.authorizeRequests()
			.antMatchers(
				"/swagger-ui.html", "/**/signup",
				"/users/login", "/users/check-email", "/user/check-nickname",
				"/email", "/verifyCode"
			)
			.permitAll()

			.antMatchers("/users", "/notification/**", "/chats/**", "/contract/**")
			.hasAnyRole(UserType.MEMBER.name(), UserType.CENTER.name())

			.antMatchers(HttpMethod.GET, "/members/**")
			.permitAll()
			.antMatchers("/members/**")
			.hasRole(UserType.MEMBER.name())

			.antMatchers(HttpMethod.GET, "/centers/**")
			.permitAll()
			.antMatchers("/centers/**")
			.hasRole(UserType.CENTER.name())

			.antMatchers(HttpMethod.GET, "/donations/**")
			.permitAll()
			.antMatchers("/donations/**/comments/**")
			.hasRole(UserType.CENTER.name())
			.antMatchers("/donations/**", "/users/donations")
			.hasRole(UserType.MEMBER.name())

			.antMatchers(HttpMethod.GET, "/wishes/**")
			.permitAll()
			.antMatchers("/wishes/**/comments/**")
			.hasRole(UserType.MEMBER.name())
			.antMatchers("/wishes/**", "/users/wishes")
			.hasRole(UserType.CENTER.name())

			.anyRequest()
			.authenticated()

			.and()
			.apply(new JwtSecurityConfig(jwtTokenProvider));
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();

		config.setAllowCredentials(true);
		config.addAllowedOriginPattern("*");
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}

}
