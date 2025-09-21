package com.elsevier.service.impl;

import com.elsevier.dao.TaskRepository;
import com.elsevier.entity.Task;
import com.elsevier.exception.GenericRestException;
import com.elsevier.service.TaskService;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl implements TaskService {

  @Autowired private TaskRepository taskRepository;

  /**
   * Retrieve all Task entities.
   *
   * @return a list containing every Task; the list may be empty if no tasks exist
   */
  @Override
  public List<Task> findAllTasks() {
    return taskRepository.findAll();
  }

  /**
   * Creates and persists a new Task.
   *
   * Validates that the task's title and description are not blank; if validation passes,
   * saves the task via the repository and returns the persisted entity.
   *
   * @param task the Task to create; its title and description must be non-blank
   * @return the persisted Task
   * @throws GenericRestException if the task's title or description is blank (results in HTTP 400)
   */
  @Override
  public Task createTask(Task task) {
    if (StringUtils.isBlank(task.getTitle()) || StringUtils.isBlank(task.getDescription())) {
      throw new GenericRestException("Tasks not saved", HttpStatus.BAD_REQUEST);
    }
    return taskRepository.save(task);
  }
}
