package com.elsevier.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.elsevier.dao.TaskRepository;
import com.elsevier.entity.Task;
import com.elsevier.exception.GenericRestException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
@DisplayName("Task Service Implementation Tests")
class TaskServiceImplTest {

  @Mock private TaskRepository taskRepository;

  @InjectMocks private TaskServiceImpl taskService;

  @Nested
  @DisplayName("Find All Tasks")
  class FindAllTasksTests {

    @Test
    @DisplayName("Should return all tasks when tasks exist")
    void shouldReturnAllTasksWhenTasksExist() {
      // Given
      List<Task> expectedTasks =
          List.of(
              createTask("Task 1", "Description 1", false),
              createTask("Task 2", "Description 2", true));
      when(taskRepository.findAll()).thenReturn(expectedTasks);

      // When
      List<Task> actualTasks = taskService.findAllTasks();

      // Then
      assertThat(actualTasks)
          .hasSize(2)
          .extracting(Task::getTitle, Task::getDescription, Task::isFlag)
          .containsExactly(
              tuple("Task 1", "Description 1", false), tuple("Task 2", "Description 2", true));
      verify(taskRepository).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no tasks exist")
    void shouldReturnEmptyListWhenNoTasksExist() {
      // Given
      when(taskRepository.findAll()).thenReturn(Collections.emptyList());

      // When
      List<Task> actualTasks = taskService.findAllTasks();

      // Then
      assertThat(actualTasks).isEmpty();
      verify(taskRepository).findAll();
    }
  }

  @Nested
  @DisplayName("Create Task")
  class CreateTaskTests {

    @Test
    @DisplayName("Should create task successfully with valid data")
    void shouldCreateTaskSuccessfullyWithValidData() {
      // Given
      Task inputTask = createTask("Valid Title", "Valid Description", true);
      Task savedTask = createTask("Valid Title", "Valid Description", true);
      when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

      // When
      Task result = taskService.createTask(inputTask);

      // Then
      assertAll(
          () -> assertThat(result).isNotNull(),
          () -> assertThat(result.getTitle()).isEqualTo("Valid Title"),
          () -> assertThat(result.getDescription()).isEqualTo("Valid Description"),
          () -> assertThat(result.isFlag()).isTrue());
      verify(taskRepository).save(inputTask);
    }

    @ParameterizedTest(name = "Should throw exception when {0}")
    @MethodSource("invalidTaskDataScenarios")
    @DisplayName("Should throw GenericRestException for invalid task data")
    void shouldThrowExceptionForInvalidTaskData(String scenario, Task invalidTask) {
      // When & Then
      assertThatThrownBy(() -> taskService.createTask(invalidTask))
          .isInstanceOf(GenericRestException.class)
          .hasMessage("Tasks not saved")
          .satisfies(
              exception -> {
                GenericRestException genericException = (GenericRestException) exception;
                assertThat(genericException.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
              });

      verify(taskRepository, never()).save(any(Task.class));
    }

    private static Stream<Arguments> invalidTaskDataScenarios() {
      return Stream.of(
          Arguments.of("title is null", createTask(null, "Valid Description", true)),
          Arguments.of("title is empty", createTask("", "Valid Description", false)),
          Arguments.of("title is blank", createTask("   ", "Valid Description", true)),
          Arguments.of("description is null", createTask("Valid Title", null, false)),
          Arguments.of("description is empty", createTask("Valid Title", "", true)),
          Arguments.of("description is blank", createTask("Valid Title", "   ", false)),
          Arguments.of("both title and description are empty", createTask("", "", true)),
          Arguments.of("both title and description are null", createTask(null, null, false)));
    }
  }

  // Test data factory method
  private static Task createTask(String title, String description, boolean flag) {
    return new Task(title, description, flag);
  }
}
