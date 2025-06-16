package com.suraj.linkedinms.user_service.service;

import com.suraj.linkedinms.user_service.entity.User;

public interface JwtService {

	String generateAccessToken(User user);
	Long getUserIdFromToken(String token);
}
