package com.elsevier.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.elsevier.entity.Task;
import com.elsevier.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(TaskController.class)
class TaskControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private TaskService taskService;

  @Test
  public void testFindAllTasks() throws Exception {
    List<Task> taskList =
        Arrays.asList(
            new Task("Test Title", "Test Description", false),
            new Task("Test Title1", "Test Description1", false));

    when(taskService.findAllTasks()).thenReturn(taskList);

    mockMvc
        .perform(get("/api/v1/tasks").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2));
  }

  @Test
  public void testFindAllTasks_StatusNotFound() throws Exception {
    List<Task> taskList = Arrays.asList();

    when(taskService.findAllTasks()).thenReturn(taskList);

    mockMvc
        .perform(get("/api/v1/tasks").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  @Test
  public void testFindAllTasks_ThrowException() throws Exception {
    List<Task> taskList = Arrays.asList();

    when(taskService.findAllTasks()).thenReturn(taskList);

    mockMvc
        .perform(get("/api/v1/tasks").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.error", is("Tasks not found")));
  }

  @Test
  public void testCreateTask() throws Exception {
    Task task = new Task("Test Title", "Test Description", false);

    when(taskService.save(task)).thenReturn(task);

    mockMvc
        .perform(
            post("/api/v1/task")
                .content(objectMapper.writeValueAsString(task))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated());

    assertEquals(taskService.save(task), task);
  }

  @Test
  public void testCreateTask_BadRequest() throws Exception {
    Task task = null;

    when(taskService.save(new Task())).thenReturn(task);

    mockMvc
        .perform(
            post("/api/v1/task")
                .content(objectMapper.writeValueAsString(task))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }
}
