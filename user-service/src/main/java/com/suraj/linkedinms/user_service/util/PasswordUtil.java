package com.suraj.linkedinms.user_service.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

	public static String hashPassword(String password) {
		// Implement your password hashing logic here
		// For example, you can use BCrypt or any other hashing algorithm
		return BCrypt.hashpw(password, BCrypt.gensalt());
	}

	public static  boolean checkPassword(String password, String hashedPassword) {
		// Implement your password checking logic here
		// For example, you can use BCrypt to verify the password
		return BCrypt.checkpw(password, hashedPassword);
	}
}
