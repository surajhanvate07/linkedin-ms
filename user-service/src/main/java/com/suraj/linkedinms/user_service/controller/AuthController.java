package com.suraj.linkedinms.user_service.controller;

import com.suraj.linkedinms.user_service.dto.LoginRequestDto;
import com.suraj.linkedinms.user_service.dto.SignUpRequestDto;
import com.suraj.linkedinms.user_service.dto.UserDto;
import com.suraj.linkedinms.user_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping("/signup")
	public ResponseEntity<UserDto> signUp(@RequestBody SignUpRequestDto signUpRequestDto) {
		UserDto cratedUser = authService.signUp(signUpRequestDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(cratedUser);
	}

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody LoginRequestDto loginRequestDto) {
		String token = authService.login(loginRequestDto);
		return ResponseEntity.ok(token);
	}
}
