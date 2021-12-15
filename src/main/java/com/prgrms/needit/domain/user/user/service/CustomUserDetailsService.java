package com.prgrms.needit.domain.user.user.service;

import com.prgrms.needit.common.error.ErrorCode;
import com.prgrms.needit.common.error.exception.NotFoundResourceException;
import com.prgrms.needit.domain.user.center.entity.Center;
import com.prgrms.needit.domain.user.center.repository.CenterRepository;
import com.prgrms.needit.domain.user.member.entity.Member;
import com.prgrms.needit.domain.user.member.repository.MemberRepository;
import java.util.Collections;
import java.util.Optional;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final MemberRepository memberRepository;
	private final CenterRepository centerRepository;

	public CustomUserDetailsService(
		MemberRepository memberRepository,
		CenterRepository centerRepository
	) {
		this.memberRepository = memberRepository;
		this.centerRepository = centerRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<Member> member = memberRepository.findByEmailAndIsDeletedFalse(email);
		if (member.isPresent()) {
			return createMemberDetails(member.get());
		}

		Optional<Center> center = centerRepository.findByEmailAndIsDeletedFalse(email);
		if (center.isPresent()) {
			return createCenterDetails(center.get());
		}

		throw new NotFoundResourceException(ErrorCode.NOT_FOUND_USER);
	}

	private UserDetails createMemberDetails(Member member) {
		return new User(member.getEmail(), member.getPassword(), Collections.singleton(
			new SimpleGrantedAuthority(member.getUserRole()
											 .getKey())));
	}

	private UserDetails createCenterDetails(Center center) {
		return new User(center.getEmail(), center.getPassword(), Collections.singleton(
			new SimpleGrantedAuthority(center.getUserRole()
											 .getKey())));
	}
}
