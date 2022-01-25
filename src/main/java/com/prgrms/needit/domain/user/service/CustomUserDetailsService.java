package com.prgrms.needit.domain.user.service;

import com.prgrms.needit.common.error.ErrorCode;
import com.prgrms.needit.common.error.exception.NotFoundResourceException;
import com.prgrms.needit.domain.user.entity.Users;
import com.prgrms.needit.domain.user.repository.UserRepository;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		return userRepository.findByEmailAndIsDeletedFalse(email)
							 .map(this::createUserDetails)
							 .orElseThrow(() ->
											  new NotFoundResourceException(
												  ErrorCode.NOT_FOUND_USER));
	}

	private UserDetails createUserDetails(Users user) {
		return new User(user.getEmail(), user.getPassword(), Collections.singleton(
			new SimpleGrantedAuthority(user.getUserRole()
										   .getKey())));
	}

}
