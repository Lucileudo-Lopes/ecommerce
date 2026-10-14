package com.lucileudo.ecommerce.security;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.lucileudo.ecommerce.user.User;
import com.lucileudo.ecommerce.user.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserRepository userRepository;

	public UserDetailsServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUserName(username)
				.orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));

		return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(),
				List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())));

	}
}
