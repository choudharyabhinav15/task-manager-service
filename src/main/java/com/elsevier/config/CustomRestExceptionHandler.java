package com.elsevier.config;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.elsevier.exception.GenericRestException;

@RestControllerAdvice
public class CustomRestExceptionHandler {

	@ExceptionHandler
	public ResponseEntity<Object> handleException(GenericRestException e){
		Map<String,Object> body = new HashMap<>();
		body.put("timestamp", new Date());
		body.put("status", e.getHttpStatus());
		body.put("error", e.getMessage());
		body.put("exception", e.getClass().getSimpleName());
		if(e.getBody() != null) {
			body.put("errorDetails", e.getBody());
		}
		return new ResponseEntity<>(body, e.getHttpStatus());
	}
}
