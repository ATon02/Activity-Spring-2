package com.mindhub.todolist.respositories;

import com.mindhub.todolist.models.UserEntity;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserEntityRepository extends JpaRepository<UserEntity,Long> {

    boolean existsByEmail (String email);

    Optional<UserEntity> findByEmail (String email);

}
