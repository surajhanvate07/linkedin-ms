package com.suraj.linkedinms.notification_service.auth;

public class UserContextHolder {

	private static final ThreadLocal<Long> currentUserId = new ThreadLocal<>();

	public static Long getCurrentUserId() {
		return currentUserId.get();
	}

	public static void setCurrentUserId(Long userId) {
		currentUserId.set(userId);
	}

	public static void clear() {
		currentUserId.remove();
	}
}
