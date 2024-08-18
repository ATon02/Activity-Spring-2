package com.mindhub.todolist.respositories;

import com.mindhub.todolist.models.Task;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task,Long> {

    List<Task> findByUser_Email(String email);
}
