package com.lucileudo.ecommerce.exceptions;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<StandardError> handleNotFound(ResourceNotFoundException e, HttpServletRequest request) {
		var error = new StandardError(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(), "Recurso não encontrado",
				e.getMessage(), request.getRequestURI());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);

	}

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<StandardError> handleBusiness(BusinessException e, HttpServletRequest request) {
		var error = new StandardError(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), "Regra de negocio violada",
				e.getMessage(), request.getRequestURI());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}
}
