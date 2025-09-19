package com.rafetcelik.dream_shops.exception;

import org.springframework.http.ResponseEntity;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex) {
		String message = "Erişim reddedildi: " + ex.getMessage();
		return new ResponseEntity<>(message, FORBIDDEN);
	}
}
