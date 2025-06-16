package com.suraj.linkedinms.user_service.service.impl;

import com.suraj.linkedinms.user_service.dto.LoginRequestDto;
import com.suraj.linkedinms.user_service.dto.SignUpRequestDto;
import com.suraj.linkedinms.user_service.dto.UserDto;
import com.suraj.linkedinms.user_service.entity.User;
import com.suraj.linkedinms.user_service.exception.AlreadyExistsException;
import com.suraj.linkedinms.user_service.exception.BadRequestException;
import com.suraj.linkedinms.user_service.exception.ResourceNotFoundException;
import com.suraj.linkedinms.user_service.repository.UserRepository;
import com.suraj.linkedinms.user_service.service.AuthService;
import com.suraj.linkedinms.user_service.service.JwtService;
import com.suraj.linkedinms.user_service.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

	private final UserRepository userRepository;
	private final ModelMapper modelMapper;
	private final JwtService jwtService;

	@Override
	public UserDto signUp(SignUpRequestDto signUpRequestDto) {
		User user = modelMapper.map(signUpRequestDto, User.class);
		user.setPassword(PasswordUtil.hashPassword(signUpRequestDto.getPassword()));

		boolean isEmailExists = userRepository.existsByEmail(user.getEmail());
		if (isEmailExists) {
			log.error("Email already exists: {}", user.getEmail());
			throw new AlreadyExistsException("Email already exists : " + user.getEmail());
		}

		try {
			User savedUser = userRepository.save(user);
			log.info("User created successfully with ID: {}", savedUser.getId());
			return modelMapper.map(savedUser, UserDto.class);
		} catch (Exception e) {
			log.error("Error occurred while creating user: {}", e.getMessage());
			throw new RuntimeException("User creation failed");
		}
	}

	@Override
	public String login(LoginRequestDto loginRequestDto) {
		User user = userRepository.findByEmail(loginRequestDto.getEmail())
				.orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + loginRequestDto.getEmail()));

		boolean isPasswordMatches = PasswordUtil.checkPassword(loginRequestDto.getPassword(), user.getPassword());
		if (!isPasswordMatches) {
			log.error("Invalid password for user: {}", user.getEmail());
			throw new BadRequestException("Invalid password");
		}

		return jwtService.generateAccessToken(user);
	}
}
