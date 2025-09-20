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

  @Override
  public List<Task> findAllTasks() {
    return taskRepository.findAll();
  }

  @Override
  public Task createTask(Task task) {
    if (StringUtils.isBlank(task.getTitle()) || StringUtils.isBlank(task.getDescription())) {
      throw new GenericRestException("Tasks not saved", HttpStatus.BAD_REQUEST);
    }
    return taskRepository.save(task);
  }
}
