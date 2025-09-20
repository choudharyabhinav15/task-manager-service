package com.elsevier.controller;

import com.elsevier.entity.Task;
import com.elsevier.exception.GenericRestException;
import com.elsevier.service.TaskService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class TaskController {

  @Autowired private TaskService taskService;

  @GetMapping("/tasks")
  public ResponseEntity<List<Task>> findAllTasks() {
    List<Task> taskList = taskService.findAllTasks();
    if (!CollectionUtils.isEmpty(taskList)) {
      return new ResponseEntity<List<Task>>(taskList, HttpStatus.OK);
    }
    throw new GenericRestException("Tasks not found", HttpStatus.NOT_FOUND);
  }

  @PostMapping("/task")
  public ResponseEntity<Task> createTask(@RequestBody Task task) {
    if (task != null) {
      return new ResponseEntity<Task>(taskService.save(task), HttpStatus.CREATED);
    }
    throw new GenericRestException("Tasks not saved", HttpStatus.BAD_REQUEST);
  }
}
