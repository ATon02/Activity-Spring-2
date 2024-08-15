package com.mindhub.todolist.services.impl;

import com.mindhub.todolist.dtos.DTOTask;
import com.mindhub.todolist.exceptions.InvalidFormatException;
import com.mindhub.todolist.exceptions.InvalidObject;
import com.mindhub.todolist.exceptions.NotCreateException;
import com.mindhub.todolist.exceptions.NotFoundException;
import com.mindhub.todolist.exceptions.NotUpdateException;
import com.mindhub.todolist.models.Task;
import com.mindhub.todolist.models.enums.TaskStatus;
import com.mindhub.todolist.respositories.TaskRepository;
import com.mindhub.todolist.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Override
    public DTOTask create(Task task) {
        try {
            if (!task.validObject()) {
                throw new InvalidObject("Check data contain empty fields");
            }
            Task taskSave = taskRepository.save(task);
            return new DTOTask(taskSave);
        } catch(DataIntegrityViolationException dex){
            throw new DataIntegrityViolationException("Bad request: User with id: " + task.getUser().getId() + " no exist");
        } catch (HttpMessageNotReadableException ex) {
            throw new NotCreateException("Task Not Created");
        }catch (InvalidObject ex) {
            throw new InvalidObject("Check data contain empty fields");
        }catch (Exception ex) {
            throw new NotCreateException("Task Not Created");
        }
    }

    @Override
    public DTOTask fetch(String id) {
        try {
            long taskId = Long.parseLong(id);
            if (taskId > 0) {
                Task taskFetch = taskRepository.findById(taskId).orElseThrow(()->new NotFoundException("Not Found Task With Id: " + id));
                return new DTOTask(taskFetch);
            }
            throw new InvalidFormatException("Bad request: Invalid ID format, id is lower or equals to 0");
        } catch (NumberFormatException ex) {
            throw new NumberFormatException("Bad request: Invalid ID format, id is not a number");
        }
    }

    @Override
    public DTOTask update(String id, Task task)  {
        try {
            long taskId = Long.parseLong(id);
            if (taskId > 0) {
                Task taskOld = taskRepository.findById(taskId).orElseThrow(() -> new NotFoundException("Not Found Task With Id: " + id));
                if (taskOld.equals(task)) {
                    throw new NotUpdateException("No Fields Were Updated For Task With Id: " + id);
                }
                if (!task.validObject()) {
                    throw new InvalidObject("Check data contain empty fields");
                }
                if (task.getUser() != null && !task.getUser().equals(taskOld.getUser())) {
                    taskOld.setUser(task.getUser());
                }
                taskOld.setDescription(valueToSave(task.getDescription(),taskOld.getDescription()));
                taskOld.setStatus(TaskStatus.valueOf(valueToSave(task.getStatus().toString(),taskOld.getStatus().toString())));
                taskOld.setTitle(valueToSave(task.getTitle(), taskOld.getTitle()));
                Task taskUpdate = taskRepository.save(taskOld);
                return new DTOTask(taskUpdate);
            } else {
                throw new InvalidFormatException("Bad request: Invalid ID format, id is lower or equals to 0");
            }
        } catch (NumberFormatException ex) {
            throw new InvalidFormatException("Bad request: Invalid ID format, id is not a number");
        } catch(DataIntegrityViolationException dex){
            throw new DataIntegrityViolationException("Bad request: User with id: " + task.getUser().getId() + " no exist");
        } catch (InvalidObject ex) {
            throw new InvalidObject("Check data contain empty fields");
        }
    }

    @Override
    public void delete(String id) {
        try {
            long taskId = Long.parseLong(id);
            if (taskId > 0) {
                taskRepository.deleteById(taskId);
            }else{
                throw new InvalidFormatException("Bad request: Invalid ID format, id is lower or equals to 0");
            }
        } catch (NumberFormatException ex) {
            throw new NumberFormatException("Bad request: Invalid ID format, id is not a number");
        }
    }

    @Override
    public List<DTOTask> fetchAll() {
        try {
            List<Task> tasks =  taskRepository.findAll();
            List<DTOTask> DtosTasks = new ArrayList<>();
            for(Task task : tasks){
                DtosTasks.add(new DTOTask(task));
            }
            return DtosTasks;
        } catch (Exception ex) {
            throw new NumberFormatException("Not Found");
        }
    }

    private String valueToSave(String newValue, String oldValue){
        return (newValue != null && !newValue.equals(oldValue)) ? newValue : oldValue;
    }
}
