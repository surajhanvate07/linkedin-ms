package com.suraj.linkedinms.notification_service.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class UserInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		Long userId = request.getHeader("X-User-Id") != null ? Long.valueOf(request.getHeader("X-User-Id")) : null;
		if(userId != null) {
			UserContextHolder.setCurrentUserId(userId);
		} else {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User ID is required");
			return false;
		}
		return HandlerInterceptor.super.preHandle(request, response, handler);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		UserContextHolder.clear();
	}
}
