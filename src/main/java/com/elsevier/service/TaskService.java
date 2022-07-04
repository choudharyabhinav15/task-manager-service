package com.elsevier.service;

import java.util.List;

import com.elsevier.entity.Task;

public interface TaskService {

	List<Task> findAllTasks();

	Task save(Task task);

}
