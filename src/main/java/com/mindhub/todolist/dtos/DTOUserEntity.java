package com.mindhub.todolist.dtos;

import com.mindhub.todolist.models.UserEntity;

public class DTOUserEntity {

    private long id;
    private String username;
    private String email;

    public DTOUserEntity(UserEntity user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
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
}
