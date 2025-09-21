package com.elsevier.service;

import com.elsevier.entity.Task;
import java.util.List;

public interface TaskService {

  /**
 * Retrieve all Task entities.
 *
 * @return an unmodifiable List containing all persisted Task instances; may be empty but never null
 */
List<Task> findAllTasks();

  /**
 * Creates and persists a new Task.
 *
 * Creates a new Task entity from the provided Task object, persists it, and returns the persisted instance
 * (which may include generated identifiers or other updated fields).
 *
 * @param task the Task to create; must not be null
 * @return the persisted Task instance
 */
Task createTask(Task task);
}
