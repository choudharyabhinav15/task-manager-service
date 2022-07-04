package com.elsevier.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.elsevier.entity.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>{

}
