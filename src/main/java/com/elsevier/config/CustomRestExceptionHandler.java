package com.elsevier.config;

import com.elsevier.exception.GenericRestException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomRestExceptionHandler {

  /**
   * Handles a GenericRestException thrown by REST controllers and converts it into an HTTP response.
   *
   * The response body is a JSON object containing:
   * - "timestamp": current date/time
   * - "status": the HTTP status from the exception
   * - "error": the exception message
   * - "exception": the exception's simple class name
   * - "errorDetails": the exception body, included only if present
   *
   * @param e the GenericRestException to convert into an HTTP response
   * @return a ResponseEntity with status taken from the exception and a body containing the fields listed above
   */
  @ExceptionHandler
  public ResponseEntity<Object> handleException(GenericRestException e) {
    Map<String, Object> body = new HashMap<>();
    body.put("timestamp", new Date());
    body.put("status", e.getHttpStatus());
    body.put("error", e.getMessage());
    body.put("exception", e.getClass().getSimpleName());
    if (e.getBody() != null) {
      body.put("errorDetails", e.getBody());
    }
    return new ResponseEntity<>(body, e.getHttpStatus());
  }
}
