package com.mindhub.todolist.services;

import com.mindhub.todolist.dtos.DTOUserEntity;
import com.mindhub.todolist.models.UserEntity;
import com.mindhub.todolist.models.enums.UserRoles;
import com.mindhub.todolist.respositories.UserEntityRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    @MockBean
    private UserEntityRepository userRepository;

    @Autowired
    private UserEntityService userService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    public void testGetUserByEmailSuccess() {
        UserEntity user = new UserEntity("john",passwordEncoder.encode("020202020"),"demotest@demo.com", UserRoles.ADMIN);
        when(userRepository.findByEmail("demotest@demo.com")).thenReturn(Optional.of(user));
        DTOUserEntity result = userService.fetchByEmail("demotest@demo.com");
        assertEquals("john", result.getUsername());
    }

    @Test
    public void testGetUserByEmailFail() {
        when(userRepository.findByEmail("demo@demo.com")).thenReturn(Optional.empty());
        Exception exception = assertThrows(NumberFormatException.class, () -> {
            userService.fetchByEmail("demo@demo.com");
        });
        assertEquals("Bad request: Unsuccessful fetch", exception.getMessage());
    }
}