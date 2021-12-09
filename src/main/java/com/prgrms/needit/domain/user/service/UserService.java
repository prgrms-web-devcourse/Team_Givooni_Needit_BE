package com.prgrms.needit.domain.user.service;

import com.prgrms.needit.common.config.jwt.JwtTokenProvider;
import com.prgrms.needit.common.error.ErrorCode;
import com.prgrms.needit.common.error.exception.NotFoundResourceException;
import com.prgrms.needit.domain.center.entity.Center;
import com.prgrms.needit.domain.center.repository.CenterRepository;
import com.prgrms.needit.domain.member.entity.Member;
import com.prgrms.needit.domain.member.repository.MemberRepository;
import com.prgrms.needit.domain.user.dto.LoginRequest;
import com.prgrms.needit.domain.user.dto.TokenResponse;
import java.util.Optional;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	private final MemberRepository memberRepository;
	private final CenterRepository centerRepository;
	private final JwtTokenProvider jwtTokenProvider;
	private final AuthenticationManagerBuilder authManagerBuilder;

	public UserService(
		MemberRepository memberRepository,
		CenterRepository centerRepository,
		JwtTokenProvider jwtTokenProvider,
		AuthenticationManagerBuilder authManagerBuilder
	) {
		this.memberRepository = memberRepository;
		this.centerRepository = centerRepository;
		this.jwtTokenProvider = jwtTokenProvider;
		this.authManagerBuilder = authManagerBuilder;
	}

	public TokenResponse login(LoginRequest login) {
		findActiveMemberAndCenter(login.getEmail());

		UsernamePasswordAuthenticationToken authenticationToken =
			new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword());

		Authentication authentication = authManagerBuilder.getObject()
														  .authenticate(authenticationToken);

		return jwtTokenProvider.generateToken(authentication);
	}

	private void findActiveMemberAndCenter(String email) {
		Optional<Member> member = memberRepository
			.findByEmailAndIsDeletedFalse(email);

		Optional<Center> center = centerRepository
			.findByEmailAndIsDeletedFalse(email);

		if (member.isEmpty() && center.isEmpty()) {
			throw new NotFoundResourceException(ErrorCode.NOT_FOUND_USER);
		}
	}

}
