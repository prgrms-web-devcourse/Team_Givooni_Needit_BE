package com.prgrms.needit.domain.user.login.service;

import com.prgrms.needit.common.config.jwt.JwtTokenProvider;
import com.prgrms.needit.common.enums.UserType;
import com.prgrms.needit.common.error.ErrorCode;
import com.prgrms.needit.common.error.exception.NotFoundResourceException;
import com.prgrms.needit.domain.user.center.entity.Center;
import com.prgrms.needit.domain.user.center.repository.CenterRepository;
import com.prgrms.needit.domain.user.login.dto.CurUser;
import com.prgrms.needit.domain.user.login.dto.LoginRequest;
import com.prgrms.needit.domain.user.login.dto.TokenResponse;
import com.prgrms.needit.domain.user.member.entity.Member;
import com.prgrms.needit.domain.user.member.repository.MemberRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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

	public CurUser getCurUser() {
		Optional<Member> member = getCurMember();
		Optional<Center> center = getCurCenter();

		if (member.isPresent()) {
			return CurUser.toResponse(member.get());
		}

		if (center.isPresent()) {
			return CurUser.toResponse(center.get());
		}

		throw new NotFoundResourceException(ErrorCode.NOT_FOUND_USER);
	}

	public Optional<Center> getCurCenter() {
		final Authentication authentication = getAuthentication();
		String userRole = getUserRole(authentication);

		if (userRole.equals(UserType.CENTER.getKey())) {
			return centerRepository.findByEmailAndIsDeletedFalse(
				authentication.getName());
		}

		return Optional.empty();
	}

	public Optional<Member> getCurMember() {
		final Authentication authentication = getAuthentication();
		String userRole = getUserRole(authentication);

		if (userRole.equals(UserType.MEMBER.getKey())) {
			return memberRepository.findByEmailAndIsDeletedFalse(
				authentication.getName());
		}

		return Optional.empty();
	}

	private Authentication getAuthentication() {
		final Authentication authentication = SecurityContextHolder.getContext()
																   .getAuthentication();
		if (authentication == null || authentication.getName() == null) {
			throw new RuntimeException("No authentication information.");
		}
		return authentication;
	}

	private String getUserRole(Authentication authentication) {
		List<String> authorities = new ArrayList<>();
		for (GrantedAuthority auth : authentication.getAuthorities()) {
			authorities.add(auth.getAuthority());
		}

		return authorities.get(0);
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
