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

  /**
   * Retrieve all tasks.
   *
   * Returns a 200 OK response with the list of Task entities when one or more tasks exist;
   * throws a GenericRestException with 404 Not Found when no tasks are present.
   *
   * @return ResponseEntity containing the list of tasks with HTTP 200 status when non-empty
   * @throws GenericRestException if no tasks are found (results in HTTP 404)
   */
  @GetMapping("/tasks")
  public ResponseEntity<List<Task>> findAllTasks() {
    List<Task> taskList = taskService.findAllTasks();
    if (!CollectionUtils.isEmpty(taskList)) {
      return new ResponseEntity<List<Task>>(taskList, HttpStatus.OK);
    }
    throw new GenericRestException("Tasks not found", HttpStatus.NOT_FOUND);
  }

  /**
   * Create a new Task from the request body and return it with HTTP 201 (Created).
   *
   * @param task the Task payload to create (bound from the request body)
   * @return the created Task wrapped in a ResponseEntity with status 201 (Created)
   */
  @PostMapping("/task")
  public ResponseEntity<Task> createTask(@RequestBody Task task) {
    return new ResponseEntity<Task>(taskService.createTask(task), HttpStatus.CREATED);
  }
}
