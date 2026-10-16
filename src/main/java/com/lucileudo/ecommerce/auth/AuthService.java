package com.lucileudo.ecommerce.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lucileudo.ecommerce.exceptions.BusinessException;
import com.lucileudo.ecommerce.security.JwtService;
import com.lucileudo.ecommerce.user.Role;
import com.lucileudo.ecommerce.user.User;
import com.lucileudo.ecommerce.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;

	public User register(RegisterDTO dto) {
		if (userRepository.findByUserName(dto.userName()).isPresent()) {
			throw new BusinessException("Usuario ja existe");
		}

		User user = new User();
		user.setName(dto.name());
		user.setUserName(dto.userName());
		user.setPassword(passwordEncoder.encode(dto.password()));
		user.setRole(Role.CLIENTE);

		return userRepository.save(user);
	}

	public String login(LoginDTO dto) {
		User user = userRepository.findByUserName(dto.userName())
				.orElseThrow(() -> new BusinessException("Credenciais invalidas"));

		if (!passwordEncoder.matches(dto.password(), user.getPassword())) {
			throw new BusinessException("Credenciais invalidas");
		}

		return jwtService.generateToken(user.getUserName(), user.getRole().name());
	}
}
