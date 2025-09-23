package com.elsevier;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Task Manager Service Application Tests")
class TaskManagerServiceApplicationTests {

  @Test
  @DisplayName("Should load Spring application context successfully")
  void shouldLoadApplicationContextSuccessfully() {}
}
