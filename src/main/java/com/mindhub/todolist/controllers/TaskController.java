package com.mindhub.todolist.controllers;

import com.mindhub.todolist.dtos.DTOTask;
import com.mindhub.todolist.models.Task;
import com.mindhub.todolist.services.TaskService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping(value = "/")
    @Operation(summary = "Create Task", description = "Save task in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task saved correctly"),
            @ApiResponse(responseCode = "400", description = "Bad request: Task Not Created or Bad request: User with id no exist", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
    })
    public ResponseEntity<DTOTask> createTask(
            @RequestBody @Schema(description = "The data task to save", implementation = Task.class) Task task) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.taskService.create(task));
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "Get Task By Id", description = "Return a task id to get.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task data found."),
            @ApiResponse(responseCode = "400", description = "Bad request: Invalid ID format, id is lower or equals to 0 or Invalid ID format, id is not a number or Bad info or Not Found Task With Id", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
    })
    public ResponseEntity<DTOTask> fetchTask(
            @PathVariable @Parameter(description = "Id to search, this id only accept numbers") String id) {
        return ResponseEntity.ok(this.taskService.fetch(id));
    }

    @PutMapping(value = "/{id}")
    @Operation(summary = "Update Task By Id", description = "Update task in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task updated correctly"),
            @ApiResponse(responseCode = "400", description = "Bad request: No Fields Were Updated For Task With Id or Invalid ID format, id is lower or equals to 0 or Invalid ID format, id is not a number or User with id no exist or Not Found Task With Id", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
    })
    public ResponseEntity<DTOTask> updateTask(
            @PathVariable @Parameter(description = "Id to update, this id only accept numbers") String id,
            @RequestBody @Schema(example = "{\"title\": \"title test\",\"description\": \"description test\",\"status\": \"PENDING\"}") Task task) {
        return ResponseEntity.ok(this.taskService.update(id, task));
    }


    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Delete Task By Id", description = "Delete task in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Task delete."),
            @ApiResponse(responseCode = "400", description = "Bad request: Invalid ID format, id is not a number or Invalid ID format, id is lower or equals to 0", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
    })
    public ResponseEntity<String> deleteTask(
            @PathVariable @Parameter(description = "Id to delete, this id only accept numbers") String id) {
        this.taskService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/")
    @Operation(summary = "Get All Tasks", description = "Return all tasks of system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tasks data found."),
            @ApiResponse(responseCode = "400", description = "Bad request: Not Found"),
    })
    public ResponseEntity<List<DTOTask>> getAll() {
        return ResponseEntity.ok(this.taskService.fetchAll());
    }

}
