package com.mindhub.todolist.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mindhub.todolist.dtos.LoginUser;
import com.mindhub.todolist.services.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Get Token Access", description = "Get a token access to system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token Access."),
            @ApiResponse(responseCode = "400", description = "Bad request: Credentials not valid or Authentication failed or Access Denied", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
    })
    public ResponseEntity<String> authenticateUser(
        @RequestBody 
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = LoginUser.class),
                examples = {
                    @ExampleObject(name = "Admin Example", value = "{\"email\": \"demo@demo.com\",\"password\": \"020202020\"}"),
                    @ExampleObject(name = "User Example", value = "{\"email\": \"demo1@demo.com\",\"password\": \"0101010101\"}"),
                    @ExampleObject(name = "Coordinator Example", value = "{\"email\": \"demo2@demo.com\",\"password\": \"0303030303\"}")
                }
            )
        ) LoginUser loginRequest) {
    return ResponseEntity.ok(this.authService.login(loginRequest));
}

    @PostMapping(value = "/register")
    @Operation(summary = "Create User With Rol User", description = "Save user in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User saved correctly"),
            @ApiResponse(responseCode = "400", description = "Bad request: Email is duplicated or The email format is not valid or UserEntity Not Created or Check data contain empty fields", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
    })
    @SecurityRequirement(name = "bearerAuth") 
    public ResponseEntity<String> createUserEntity(
            @RequestBody() @Schema(example = "{\"email\": \"email@test.com\",\"password\": \"passwordtest\"}") LoginUser loginRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.authService.register(loginRequest));
    }
}

