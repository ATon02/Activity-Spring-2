package com.mindhub.todolist.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.util.Objects;

import com.mindhub.todolist.models.enums.TaskStatus;

@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(hidden = true) 
    private long id;
    @Schema(example = "title test") 
    private String title;
    @Schema(example = "description test") 
    private String description;
    @Enumerated(EnumType.STRING)
    @Schema(example = "PENDING") 
    private TaskStatus status;
    @ManyToOne(fetch = FetchType.LAZY)
    @Schema(example = "{\"id\": 1}")
    private UserEntity user;

    public Task() {
    }

    public Task(String title, TaskStatus status, String description) {
        this.title = title;
        this.status = status;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(this.title, task.title) && Objects.equals(this.description, task.description) && this.status == task.status;
    }

    public boolean validObject(){
        return !this.title.isBlank() && !this.description.isBlank() && this.status!=null;
    }

}
