package com.suraj.linkedinms.user_service.service;

import com.suraj.linkedinms.user_service.dto.LoginRequestDto;
import com.suraj.linkedinms.user_service.dto.SignUpRequestDto;
import com.suraj.linkedinms.user_service.dto.UserDto;

public interface AuthService {
	UserDto signUp(SignUpRequestDto signUpRequestDto);

	String login(LoginRequestDto loginRequestDto);
}
