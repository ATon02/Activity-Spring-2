package com.mindhub.todolist.controllers.user;

import com.mindhub.todolist.configurationsjwt.JwtUtils;
import com.mindhub.todolist.dtos.DTOTask;
import com.mindhub.todolist.models.Task;
import com.mindhub.todolist.services.TaskService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;
    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping(value = "/")
    @Operation(summary = "Create Task For User Authenticated", description = "Save task in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task saved correctly"),
            @ApiResponse(responseCode = "400", description = "Bad request: Task Not Created or Bad request: User with id no exist", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "403", description = "Not authorized for this request", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "401", description = "Invalid Token", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string")))
    })
    @SecurityRequirement(name = "bearerAuth") 
    public ResponseEntity<DTOTask> createTask(
            @RequestBody @Schema(description = "The data task to save", example = "{\"title\": \"title test\",\"description\": \"description test\",\"status\": \"PENDING\"}") Task task, 
            @RequestHeader("Authorization") @Parameter(hidden = true) String authorizationHeader) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.taskService.createByUser(task,jwtUtils.extractUserId(authorizationHeader)));
    }

    @PutMapping(value = "/{id}")
    @Operation(summary = "Update Task By Id", description = "Update task in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task updated correctly"),
            @ApiResponse(responseCode = "400", description = "Bad request: No Fields Were Updated For Task With Id or Invalid ID format, id is lower or equals to 0 or Invalid ID format, id is not a number or User with id no exist or Not Found Task With Id", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "403", description = "Not authorized for this request", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "401", description = "Invalid Token", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string")))
    })
    @SecurityRequirement(name = "bearerAuth") 
    public ResponseEntity<DTOTask> updateTask(
            @PathVariable @Parameter(description = "Id to update, this id only accept numbers") String id,
            @RequestBody @Schema(example = "{\"title\": \"title test\",\"description\": \"description test\",\"status\": \"PENDING\"}") Task task,
            @RequestHeader("Authorization") @Parameter(hidden = true) String authorizationHeader) {
        return ResponseEntity.ok(this.taskService.updateByUser(id, task,jwtUtils.extractUserId(authorizationHeader)));
    }

    @GetMapping(value = "/")
    @Operation(summary = "Get All Tasks For User Authenticated", description = "Return all tasks of system for User Authenticated.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tasks data found."),
            @ApiResponse(responseCode = "400", description = "Bad request: Not Found",content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "403", description = "Not authorized for this request", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "401", description = "Invalid Token", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string")))
    })
    @SecurityRequirement(name = "bearerAuth") 
    public ResponseEntity<List<DTOTask>> getAll(Authentication authentication) {
        return ResponseEntity.ok(this.taskService.fetchByUser(authentication.getName()));
    }

}
