package com.suraj.linkedinms.user_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ApiError> handleRuntimeException(RuntimeException ex) {
		ApiError apiError = new ApiError(ex.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		return ResponseEntity.status(500).body(apiError);
	}

	@ExceptionHandler(AlreadyExistsException.class)
	public ResponseEntity<ApiError> handleAlreadyExistsException(AlreadyExistsException ex) {
		ApiError apiError = new ApiError(ex.getLocalizedMessage(), HttpStatus.CONFLICT);
		return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiError> handleResourceNotFoundException(ResourceNotFoundException ex) {
		ApiError apiError = new ApiError(ex.getLocalizedMessage(), HttpStatus.NOT_FOUND);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
	}

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<ApiError> handleBadRequestException(BadRequestException ex) {
		ApiError apiError = new ApiError(ex.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiError> handleGeneric(Exception ex) {
		ApiError apiError = new ApiError("Unexpected error", HttpStatus.INTERNAL_SERVER_ERROR);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
	}

	@ExceptionHandler({NullPointerException.class, IllegalArgumentException.class})
	public ResponseEntity<ApiError> handleSelectedExceptions(Exception ex) {
		ApiError apiError;
		if (ex instanceof NullPointerException) {
			apiError = new ApiError("Null value found", HttpStatus.INTERNAL_SERVER_ERROR);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
		} else if (ex instanceof IllegalArgumentException) {
			apiError = new ApiError("Invalid argument", HttpStatus.BAD_REQUEST);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
		}
		apiError = new ApiError("Unhandled error", HttpStatus.INTERNAL_SERVER_ERROR);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
	}

}
