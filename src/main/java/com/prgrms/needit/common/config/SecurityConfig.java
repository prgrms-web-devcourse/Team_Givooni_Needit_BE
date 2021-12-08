package com.prgrms.needit.common.config;

import com.prgrms.needit.domain.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private static final String[] AUTH_WHITELIST = {
		"/swagger-resources/**",
		"/swagger-ui.html",
		"/v2/api-docs",
		"/webjars/**"
	};
	@Autowired
	private MemberService memberService;

	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// FilterChainProxy를 생성하는 필터; 인증 대상에서 제외
	@Override
	public void configure(WebSecurity web) {
		web.ignoring()
		   .antMatchers(AUTH_WHITELIST);
	}

	// HTTP 요청에 대한 보안을 설정
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf()
			.disable();

		http.authorizeRequests()
			.antMatchers("/member/**")
			.authenticated()
			.antMatchers("/admin/**")
			.authenticated()
			.antMatchers("/**")
			.permitAll();

		// 로그인 설정
		http.formLogin()
			.loginPage("/login")
			.defaultSuccessUrl("/")
			.permitAll();

		// 로그아웃 설정
		http.logout()
			.logoutRequestMatcher(
				new AntPathRequestMatcher("/logout"))    // 사용자 인증
			.logoutSuccessUrl("/login")
			.invalidateHttpSession(true);

		// 권한이 없는 사용자가 접근했을 경우 이동할 경로
		http.exceptionHandling()
			.accessDeniedPage("/denied");
	}

	// 사용자 인증
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(memberService)
			.passwordEncoder(getPasswordEncoder());
	}
}
