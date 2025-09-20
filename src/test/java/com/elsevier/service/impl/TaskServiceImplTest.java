package com.elsevier.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.elsevier.dao.TaskRepository;
import com.elsevier.entity.Task;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

  @InjectMocks private TaskServiceImpl taskServiceImpl;

  @Mock private TaskRepository taskRepository;

  @Test
  public void testFindAllTasks() {
    List<Task> taskList =
        Arrays.asList(
            new Task("Test Title", "Test Description", false),
            new Task("Test Title1", "Test Description1", false));
    when(taskServiceImpl.findAllTasks()).thenReturn(taskList);

    assertEquals(taskServiceImpl.findAllTasks().size(), 2);
  }

  @Test
  public void testSave() {
    Task task = new Task("Test Title", "Test Description", false);

    taskServiceImpl.save(task);

    verify(taskRepository, times(1)).save(task);
  }
}
