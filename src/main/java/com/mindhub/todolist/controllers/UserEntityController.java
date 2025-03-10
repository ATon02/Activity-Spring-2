package com.mindhub.todolist.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mindhub.todolist.dtos.DTOUserEntity;
import com.mindhub.todolist.models.UserEntity;
import com.mindhub.todolist.services.UserEntityService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/users")
public class UserEntityController {
    @Autowired
    private UserEntityService userEntityService;

    @PostMapping(value = "/")
    @Operation(summary = "Create User", description = "Save user in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User saved correctly"),
            @ApiResponse(responseCode = "400", description = "Bad request: Email is duplicated or The email format is not valid or UserEntity Not Created or Check data contain empty fields", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
    })
    public ResponseEntity<DTOUserEntity> createUserEntity(
            @RequestBody() @Schema(example = "{\"username\": \"username test\",\"email\": \"email@test.com\",\"password\": \"password test\"}") UserEntity userEntity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userEntityService.create(userEntity));
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "Get User By Id", description = "Return a user id to get.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User data found."),
            @ApiResponse(responseCode = "400", description = "Bad request: Invalid ID format, id is lower or equals to 0 or Invalid ID format, id is not a number or Bad info or Not Found UserEntity", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
    })
    public ResponseEntity<DTOUserEntity> fetchUserEntity(
            @PathVariable(name = "id") @Parameter(description = "Id to search, this id only accept numbers") String id) {
        return ResponseEntity.ok(this.userEntityService.fetch(id));
    }

    @PutMapping(value = "/{id}")
    @Operation(summary = "Update User By Id", description = "Update user in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User updated correctly"),
            @ApiResponse(responseCode = "400", description = "Bad request: Email is duplicated or The email format is not valid or Check data contain empty fields or UserEntity Not Created or Invalid ID format, id is not a number or Invalid ID format, id is lower or equals to 0 or No Fields Were Updated For UserEntity With Id or Not Found UserEntity With Id", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
    })
    public ResponseEntity<DTOUserEntity> updateUserEntity(
            @PathVariable @Parameter(description = "Id to update, this id only accept numbers") String id,
            @RequestBody @Schema(example = "{\"username\": \"username test\",\"email\": \"email1@test.com\",\"password\": \"password test\"}") UserEntity userEntity) {
        return ResponseEntity.ok(this.userEntityService.update(id, userEntity));
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Delete User By Id", description = "Delete user in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User delete."),
            @ApiResponse(responseCode = "400", description = "Bad request: Invalid ID format, id is not a number or User with id is related to tasks or Invalid ID format, id is lower or equals to 0", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
    })
    public ResponseEntity<String> deleteUserEntity(
            @PathVariable @Parameter(description = "Id to delete, this id only accept numbers") String id) {
        this.userEntityService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/")
    @Operation(summary = "Get All Users", description = "Return all users of system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users data found."),
            @ApiResponse(responseCode = "400", description = "Bad request: Not Found"),
    })
    public ResponseEntity<List<DTOUserEntity>> getAll() {
        return ResponseEntity.ok(this.userEntityService.fetchAll());
    }
}
