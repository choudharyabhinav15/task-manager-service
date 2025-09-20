package com.elsevier.exception;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;

@DisplayName("Generic REST Exception Tests")
class GenericRestExceptionTest {

  @Nested
  @DisplayName("Constructor Tests")
  class ConstructorTests {

    @Test
    @DisplayName("Should create exception with default constructor")
    void shouldCreateExceptionWithDefaultConstructor() {
      // When
      GenericRestException exception = new GenericRestException();

      // Then
      assertAll(
          () -> assertThat(exception.getMessage()).isNull(),
          () -> assertThat(exception.getHttpStatus()).isNull(),
          () -> assertThat(exception.getBody()).isNull());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"Test error message", "Another error", "   ", "Error with spaces"})
    @DisplayName("Should create exception with message constructor")
    void shouldCreateExceptionWithMessageConstructor(String errorMessage) {
      // When
      GenericRestException exception = new GenericRestException(errorMessage);

      // Then
      assertAll(
          () -> assertThat(exception.getMessage()).isEqualTo(errorMessage),
          () -> assertThat(exception.getHttpStatus()).isNull(),
          () -> assertThat(exception.getBody()).isNull());
    }

    @ParameterizedTest
    @EnumSource(HttpStatus.class)
    @DisplayName("Should create exception with message and HTTP status")
    void shouldCreateExceptionWithMessageAndHttpStatus(HttpStatus httpStatus) {
      // Given
      String errorMessage = "Test error for " + httpStatus.name();

      // When
      GenericRestException exception = new GenericRestException(errorMessage, httpStatus);

      // Then
      assertAll(
          () -> assertThat(exception.getMessage()).isEqualTo(errorMessage),
          () -> assertThat(exception.getHttpStatus()).isEqualTo(httpStatus),
          () -> assertThat(exception.getBody()).isNull());
    }

    @ParameterizedTest(name = "Should create exception with all parameters: {0}")
    @MethodSource("fullConstructorScenarios")
    @DisplayName("Should create exception with message, HTTP status and body")
    void shouldCreateExceptionWithAllParameters(
        String scenario, String message, HttpStatus status, Object body) {
      // When
      GenericRestException exception = new GenericRestException(message, status, body);

      // Then
      assertAll(
          () -> assertThat(exception.getMessage()).isEqualTo(message),
          () -> assertThat(exception.getHttpStatus()).isEqualTo(status),
          () -> assertThat(exception.getBody()).isEqualTo(body));
    }

    private static Stream<Arguments> fullConstructorScenarios() {
      return Stream.of(
          Arguments.of("string body", "Error message", HttpStatus.BAD_REQUEST, "String body"),
          Arguments.of("null body", "Error message", HttpStatus.INTERNAL_SERVER_ERROR, null),
          Arguments.of(
              "object body",
              "Error message",
              HttpStatus.NOT_FOUND,
              new TestErrorBody("field", "error")),
          Arguments.of("empty string body", "Error message", HttpStatus.CONFLICT, ""),
          Arguments.of("numeric body", "Error message", HttpStatus.UNAUTHORIZED, 42),
          Arguments.of("boolean body", "Error message", HttpStatus.FORBIDDEN, true));
    }
  }

  @Nested
  @DisplayName("Getter Tests")
  class GetterTests {

    @Test
    @DisplayName("Should return correct body")
    void shouldReturnCorrectBody() {
      // Given
      Object expectedBody = "Test body";
      GenericRestException exception =
          new GenericRestException("Error", HttpStatus.OK, expectedBody);

      // When
      Object actualBody = exception.getBody();

      // Then
      assertThat(actualBody).isEqualTo(expectedBody);
    }

    @Test
    @DisplayName("Should return correct HTTP status")
    void shouldReturnCorrectHttpStatus() {
      // Given
      HttpStatus expectedStatus = HttpStatus.NOT_FOUND;
      GenericRestException exception = new GenericRestException("Error", expectedStatus);

      // When
      HttpStatus actualStatus = exception.getHttpStatus();

      // Then
      assertThat(actualStatus).isEqualTo(expectedStatus);
    }

    @Test
    @DisplayName("Should return null for unset properties")
    void shouldReturnNullForUnsetProperties() {
      // Given
      GenericRestException exception = new GenericRestException("Only message");

      // When & Then
      assertAll(
          () -> assertThat(exception.getMessage()).isEqualTo("Only message"),
          () -> assertThat(exception.getHttpStatus()).isNull(),
          () -> assertThat(exception.getBody()).isNull());
    }
  }

  // Test data class for complex body scenarios
  private static class TestErrorBody {
    private final String field;
    private final String error;

    public TestErrorBody(String field, String error) {
      this.field = field;
      this.error = error;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) return true;
      if (obj == null || getClass() != obj.getClass()) return false;
      TestErrorBody that = (TestErrorBody) obj;
      return field.equals(that.field) && error.equals(that.error);
    }

    @Override
    public int hashCode() {
      return field.hashCode() + error.hashCode();
    }
  }
}
