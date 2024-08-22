package com.mindhub.todolist.dtos;

import com.mindhub.todolist.models.UserEntity;
import com.mindhub.todolist.models.enums.UserRoles;

public class DTOUserEntity {

    private long id;
    private String username;
    private String email;
    private UserRoles role; 

    public DTOUserEntity(UserEntity user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.role = user.getRole();
    }

    public DTOUserEntity(long id, String username, String email, UserRoles role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public UserRoles getRole() {
        return role;
    }
    
}
