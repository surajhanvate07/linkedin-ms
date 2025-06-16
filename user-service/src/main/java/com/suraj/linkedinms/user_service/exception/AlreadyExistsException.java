package com.suraj.linkedinms.user_service.exception;

public class AlreadyExistsException extends RuntimeException {
	public AlreadyExistsException(String message) {
		super(message);
	}
}
