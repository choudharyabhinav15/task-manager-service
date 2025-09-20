package com.elsevier.service.impl;

import com.elsevier.dao.TaskRepository;
import com.elsevier.entity.Task;
import com.elsevier.service.TaskService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl implements TaskService {

  @Autowired private TaskRepository taskRepository;

  @Override
  public List<Task> findAllTasks() {
    return taskRepository.findAll();
  }

  @Override
  public Task save(Task task) {
    return taskRepository.save(task);
  }
}
