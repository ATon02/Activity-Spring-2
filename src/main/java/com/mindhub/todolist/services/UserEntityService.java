package com.mindhub.todolist.services;

import java.util.List;

import com.mindhub.todolist.dtos.DTOUserEntity;
import com.mindhub.todolist.models.UserEntity;

public interface UserEntityService {
    DTOUserEntity create(UserEntity userEntity);
    DTOUserEntity fetch(String id);
    DTOUserEntity update(String id, UserEntity userEntity);
    void delete(String id);
    List<DTOUserEntity> fetchAll();
}
