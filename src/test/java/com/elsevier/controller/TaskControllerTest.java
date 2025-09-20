package com.elsevier.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.elsevier.entity.Task;
import com.elsevier.exception.GenericRestException;
import com.elsevier.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(TaskController.class)
@DisplayName("Task Controller Tests")
class TaskControllerTest {

  private static final String TASKS_ENDPOINT = "/api/v1/tasks";
  private static final String TASK_ENDPOINT = "/api/v1/task";

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private TaskService taskService;

  @Nested
  @DisplayName("GET /api/v1/tasks - Find All Tasks")
  class FindAllTasksTests {

    @Test
    @DisplayName("Should return OK with tasks when tasks exist")
    void shouldReturnTasksWhenTasksExist() throws Exception {
      // Given
      List<Task> tasks =
          List.of(
              createTask("Task 1", "Description 1", false),
              createTask("Task 2", "Description 2", true));
      when(taskService.findAllTasks()).thenReturn(tasks);

      // When & Then
      mockMvc
          .perform(get(TASKS_ENDPOINT).contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$", hasSize(2)))
          .andExpect(jsonPath("$[0].title", is("Task 1")))
          .andExpect(jsonPath("$[0].description", is("Description 1")))
          .andExpect(jsonPath("$[0].flag", is(false)))
          .andExpect(jsonPath("$[1].title", is("Task 2")))
          .andExpect(jsonPath("$[1].description", is("Description 2")))
          .andExpect(jsonPath("$[1].flag", is(true)));
    }

    @Test
    @DisplayName("Should return NOT_FOUND when no tasks exist")
    void shouldReturnNotFoundWhenNoTasksExist() throws Exception {
      // Given
      when(taskService.findAllTasks()).thenReturn(Collections.emptyList());

      // When & Then
      mockMvc
          .perform(get(TASKS_ENDPOINT).contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isNotFound())
          .andExpect(jsonPath("$.error", is("Tasks not found")));
    }
  }

  @Nested
  @DisplayName("POST /api/v1/task - Create Task")
  class CreateTaskTests {

    @Test
    @DisplayName("Should create task successfully with valid data")
    void shouldCreateTaskSuccessfully() throws Exception {
      // Given
      Task inputTask = createTask("New Task", "New Description", false);
      Task savedTask = createTaskWithId(1L, "New Task", "New Description", false);
      when(taskService.createTask(any(Task.class))).thenReturn(savedTask);

      // When & Then
      performPostRequest(inputTask)
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.title", is("New Task")))
          .andExpect(jsonPath("$.description", is("New Description")))
          .andExpect(jsonPath("$.flag", is(false)));
    }

    @ParameterizedTest(name = "Should return BAD_REQUEST for invalid task: {0}")
    @MethodSource("invalidTaskScenarios")
    @DisplayName("Should return BAD_REQUEST for invalid task data")
    void shouldReturnBadRequestForInvalidTask(String scenario, Task invalidTask) throws Exception {
      // Given
      when(taskService.createTask(any(Task.class)))
          .thenThrow(new GenericRestException("Tasks not saved", HttpStatus.BAD_REQUEST));

      // When & Then
      performPostRequest(invalidTask)
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.error", is("Tasks not saved")));
    }

    private static Stream<Arguments> invalidTaskScenarios() {
      return Stream.of(
          Arguments.of("empty task", new Task()),
          Arguments.of("blank title", createTask("", "Valid description", true)),
          Arguments.of("blank description", createTask("Valid title", "", false)),
          Arguments.of("null title", createTask(null, "Valid description", true)),
          Arguments.of("null description", createTask("Valid title", null, false)),
          Arguments.of("whitespace title", createTask("   ", "Valid description", true)),
          Arguments.of("whitespace description", createTask("Valid title", "   ", false)));
    }

    private ResultActions performPostRequest(Task task) throws Exception {
      return mockMvc.perform(
          post(TASK_ENDPOINT)
              .content(objectMapper.writeValueAsString(task))
              .contentType(MediaType.APPLICATION_JSON)
              .accept(MediaType.APPLICATION_JSON));
    }
  }

  // Test data factory methods
  private static Task createTask(String title, String description, boolean flag) {
    return new Task(title, description, flag);
  }

  private static Task createTaskWithId(Long id, String title, String description, boolean flag) {
    Task task = new Task(title, description, flag);
    // Note: If Task has setId method, use it here
    return task;
  }
}
