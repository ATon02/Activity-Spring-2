package com.mindhub.todolist.controllers.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mindhub.todolist.configurationsjwt.JwtUtils;
import com.mindhub.todolist.dtos.DTOUserEntity;
import com.mindhub.todolist.models.UserEntity;
import com.mindhub.todolist.services.UserEntityService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/users")
public class UserEntityController {
    @Autowired
    private UserEntityService userEntityService;
    @Autowired
    private JwtUtils jwtUtils;


    @GetMapping(value = "/")
    @Operation(summary = "Get Data User Authenticated", description = "Return a data user authenticated.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User data found."),
            @ApiResponse(responseCode = "400", description = "Bad request: Invalid ID format, id is lower or equals to 0 or Invalid ID format, id is not a number or Bad info or Not Found UserEntity", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "403", description = "Not authorized for this request", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "401", description = "Invalid Token", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string")))
    })
    @SecurityRequirement(name = "bearerAuth") 
    public ResponseEntity<DTOUserEntity> fetchUserEntity(Authentication authentication) {
        return ResponseEntity.ok(this.userEntityService.fetchByEmail(authentication.getName()));
    }

    @PutMapping(value = "/")
    @Operation(summary = "Update Self Data User Authenticated", description = "Update Data User Authenticated.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User updated correctly **Warning:** Generate a new token after updating.\""),
            @ApiResponse(responseCode = "400", description = "Bad request: Email is duplicated or The email format is not valid or Check data contain empty fields or UserEntity Not Created or Invalid ID format, id is not a number or Invalid ID format, id is lower or equals to 0 or No Fields Were Updated For UserEntity With Id or Not Found UserEntity With Id", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "403", description = "Not authorized for this request", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "401", description = "Invalid Token", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string")))
    })
    @SecurityRequirement(name = "bearerAuth") 
    public ResponseEntity<DTOUserEntity> updateUserEntity(
            @RequestHeader("Authorization") @Parameter(hidden = true) String authorizationHeader,
            @RequestBody @Schema(example = "{\"username\": \"username test\",\"email\": \"email1@test.com\",\"password\": \"passwordtest\"}") UserEntity userEntity) {
        return ResponseEntity.ok(this.userEntityService.updateSelf(jwtUtils.extractUserId(authorizationHeader), userEntity));
    }


}
