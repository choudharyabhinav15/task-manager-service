package com.elsevier.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.elsevier.exception.GenericRestException;
import java.util.Date;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@DisplayName("Custom REST Exception Handler Tests")
class CustomRestExceptionHandlerTest {

  private CustomRestExceptionHandler exceptionHandler;

  @BeforeEach
  void setUp() {
    exceptionHandler = new CustomRestExceptionHandler();
  }

  @Nested
  @DisplayName("Handle Generic REST Exception")
  class HandleGenericRestExceptionTests {

    @Test
    @DisplayName("Should handle exception with body successfully")
    void shouldHandleExceptionWithBodySuccessfully() {
      // Given
      String errorMessage = "Test error message";
      HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
      String errorDetails = "Detailed error information";
      GenericRestException exception =
          new GenericRestException(errorMessage, httpStatus, errorDetails);

      // When
      ResponseEntity<Object> response = exceptionHandler.handleException(exception);

      // Then
      assertAll(
          () -> assertThat(response.getStatusCode()).isEqualTo(httpStatus),
          () -> assertThat(response.getBody()).isInstanceOf(Map.class));

      @SuppressWarnings("unchecked")
      Map<String, Object> responseBody = (Map<String, Object>) response.getBody();

      assertAll(
          () -> assertThat(responseBody.get("error")).isEqualTo(errorMessage),
          () -> assertThat(responseBody.get("status")).isEqualTo(httpStatus),
          () -> assertThat(responseBody.get("exception")).isEqualTo("GenericRestException"),
          () -> assertThat(responseBody.get("errorDetails")).isEqualTo(errorDetails),
          () -> assertThat(responseBody.get("timestamp")).isInstanceOf(Date.class),
          () -> assertThat(responseBody).hasSize(5));
    }

    @Test
    @DisplayName("Should handle exception without body successfully")
    void shouldHandleExceptionWithoutBodySuccessfully() {
      // Given
      String errorMessage = "Test error without details";
      HttpStatus httpStatus = HttpStatus.NOT_FOUND;
      GenericRestException exception = new GenericRestException(errorMessage, httpStatus, null);

      // When
      ResponseEntity<Object> response = exceptionHandler.handleException(exception);

      // Then
      assertAll(
          () -> assertThat(response.getStatusCode()).isEqualTo(httpStatus),
          () -> assertThat(response.getBody()).isInstanceOf(Map.class));

      @SuppressWarnings("unchecked")
      Map<String, Object> responseBody = (Map<String, Object>) response.getBody();

      assertAll(
          () -> assertThat(responseBody.get("error")).isEqualTo(errorMessage),
          () -> assertThat(responseBody.get("status")).isEqualTo(httpStatus),
          () -> assertThat(responseBody.get("exception")).isEqualTo("GenericRestException"),
          () -> assertThat(responseBody.get("timestamp")).isInstanceOf(Date.class),
          () -> assertThat(responseBody).doesNotContainKey("errorDetails"),
          () -> assertThat(responseBody).hasSize(4));
    }

    @ParameterizedTest(name = "Should handle exception with {0} and {1}")
    @MethodSource("exceptionScenarios")
    @DisplayName("Should handle various exception scenarios")
    void shouldHandleVariousExceptionScenarios(
        String scenario, HttpStatus status, Object body, int expectedFieldCount) {
      // Given
      String message = "Error for scenario: " + scenario;
      GenericRestException exception = new GenericRestException(message, status, body);

      // When
      ResponseEntity<Object> response = exceptionHandler.handleException(exception);

      // Then
      assertAll(
          () -> assertThat(response.getStatusCode()).isEqualTo(status),
          () -> assertThat(response.getBody()).isInstanceOf(Map.class));

      @SuppressWarnings("unchecked")
      Map<String, Object> responseBody = (Map<String, Object>) response.getBody();

      assertAll(
          () -> assertThat(responseBody.get("error")).isEqualTo(message),
          () -> assertThat(responseBody.get("status")).isEqualTo(status),
          () -> assertThat(responseBody.get("exception")).isEqualTo("GenericRestException"),
          () -> assertThat(responseBody.get("timestamp")).isInstanceOf(Date.class),
          () -> assertThat(responseBody).hasSize(expectedFieldCount));

      if (body != null) {
        assertThat(responseBody.get("errorDetails")).isEqualTo(body);
      } else {
        assertThat(responseBody).doesNotContainKey("errorDetails");
      }
    }

    private static Stream<Arguments> exceptionScenarios() {
      return Stream.of(
          Arguments.of("string body", HttpStatus.BAD_REQUEST, "String error details", 5),
          Arguments.of("null body", HttpStatus.INTERNAL_SERVER_ERROR, null, 4),
          Arguments.of("empty string body", HttpStatus.CONFLICT, "", 5),
          Arguments.of("numeric body", HttpStatus.UNAUTHORIZED, 404, 5),
          Arguments.of("boolean body", HttpStatus.FORBIDDEN, false, 5),
          Arguments.of(
              "complex object body",
              HttpStatus.UNPROCESSABLE_ENTITY,
              Map.of("field", "value", "error", "validation failed"),
              5));
    }

    @Test
    @DisplayName("Should preserve timestamp consistency within reasonable time window")
    void shouldPreserveTimestampConsistency() {
      // Given
      GenericRestException exception = new GenericRestException("Test", HttpStatus.BAD_REQUEST);
      Date beforeCall = new Date();

      // When
      ResponseEntity<Object> response = exceptionHandler.handleException(exception);

      // Then
      Date afterCall = new Date();
      @SuppressWarnings("unchecked")
      Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
      Date timestamp = (Date) responseBody.get("timestamp");

      assertAll(
          () -> assertThat(timestamp).isAfterOrEqualTo(beforeCall),
          () -> assertThat(timestamp).isBeforeOrEqualTo(afterCall));
    }
  }
}
