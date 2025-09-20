package com.elsevier.service;

import com.elsevier.entity.Task;
import java.util.List;

public interface TaskService {

  List<Task> findAllTasks();

  Task createTask(Task task);
}
