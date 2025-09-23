package com.elsevier.entity;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "task")
public class Task {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String description;

  @Column(nullable = false)
  private boolean flag;

  /**
   * Creates a new Task instance.
   *
   * <p>Required by JPA: a public no-argument constructor used by the persistence provider when
   * instantiating entities.
   */
  public Task() {
    super();
  }

  /**
   * Constructs a Task with the specified title, description, and flag.
   *
   * <p>Title and description are expected to be non-null; the `id` is not set by this constructor
   * and will be generated when the entity is persisted.
   *
   * @param title the task title (must be non-null for persistence)
   * @param description the task description (must be non-null for persistence)
   * @param flag the task's boolean flag (e.g., completion or status indicator)
   */
  public Task(String title, String description, boolean flag) {
    super();
    this.title = title;
    this.description = description;
    this.flag = flag;
  }

  /**
   * Returns the task's title.
   *
   * @return the non-null title of the task
   */
  public String getTitle() {
    return title;
  }

  /**
   * Set the task's title.
   *
   * <p>The title is required to be non-null at the database level (the mapped column is nullable =
   * false). Passing null will set the field to null and may cause a constraint violation when the
   * entity is persisted.
   *
   * @param title the new title for the task; should not be null when the entity will be persisted
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Returns the task's description.
   *
   * @return the description text
   */
  public String getDescription() {
    return description;
  }

  /**
   * Set the task's description.
   *
   * <p>This value is mapped to a non-nullable database column.
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Returns whether this task's flag is set.
   *
   * @return true if the task's flag is set; false otherwise
   */
  public boolean isFlag() {
    return flag;
  }

  /**
   * Sets the task's flag state.
   *
   * @param flag true to set the flag, false to clear it
   */
  public void setFlag(boolean flag) {
    this.flag = flag;
  }

  /**
   * Compares this Task to another object for equality.
   *
   * <p>The comparison returns true when the other object is a Task and has the same id, flag,
   * title, and description.
   *
   * @param o the object to compare with
   * @return true if the given object is a Task with equal id, flag, title, and description; false
   *     otherwise
   */
  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    Task task = (Task) o;
    return id == task.id
        && flag == task.flag
        && Objects.equals(title, task.title)
        && Objects.equals(description, task.description);
  }

  /**
   * Computes a hash code for this Task.
   *
   * <p>The result is computed from the Task's id, title, description, and flag fields and is
   * consistent with the {@link #equals(Object)} implementation.
   *
   * @return a hash code value for this Task
   */
  @Override
  public int hashCode() {
    return Objects.hash(id, title, description, flag);
  }
}
