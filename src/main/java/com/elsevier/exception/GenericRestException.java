package com.elsevier.exception;

import org.springframework.http.HttpStatus;

public class GenericRestException extends RuntimeException {

  private static final long serialVersionUID = 4041110695238673629L;

  private Object body;

  private HttpStatus httpStatus;

  /**
   * Creates a GenericRestException with no detail message, HTTP status, or body.
   */
  public GenericRestException() {
    super();
  }

  /**
   * Constructs a GenericRestException with the specified error message.
   *
   * @param errorMessage human-readable message describing the error
   */
  public GenericRestException(String errorMessage) {
    super(errorMessage);
  }

  /**
   * Creates a GenericRestException with a message and an associated HTTP status.
   *
   * @param errorMessage human-readable error message describing the failure
   * @param httpStatus HTTP status to associate with this exception (used for REST responses)
   */
  public GenericRestException(String errorMessage, HttpStatus httpStatus) {
    super(errorMessage);
    this.httpStatus = httpStatus;
  }

  /**
   * Constructs a GenericRestException with an error message, an associated HTTP status, and an arbitrary body object.
   *
   * @param errorMessage human-readable error message
   * @param httpStatusCode HTTP status to be returned for this exception
   * @param body optional response body or additional error details (may be any object that will be serialized)
   */
  public GenericRestException(String errorMessage, HttpStatus httpStatusCode, Object body) {
    this(errorMessage, httpStatusCode);
    this.body = body;
  }

  /**
   * Returns the optional response body associated with this exception.
   *
   * The body can contain additional error details or payload to be returned to a client and may be {@code null}.
   *
   * @return the response body object, or {@code null} if none was provided
   */
  public Object getBody() {
    return body;
  }

  /**
   * Returns the HTTP status associated with this exception.
   *
   * @return the HttpStatus set for this exception, or {@code null} if none was provided
   */
  public HttpStatus getHttpStatus() {
    return httpStatus;
  }
}
