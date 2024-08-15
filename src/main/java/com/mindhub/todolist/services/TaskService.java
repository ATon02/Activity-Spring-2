package com.mindhub.todolist.services;

import com.mindhub.todolist.dtos.DTOTask;
import com.mindhub.todolist.models.Task;

import java.util.List;

public interface TaskService {

    DTOTask create(Task task);
    DTOTask fetch(String id);
    DTOTask update(String id, Task task);
    void delete(String id);
    List<DTOTask> fetchAll();
}
