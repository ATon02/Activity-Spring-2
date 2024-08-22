package com.mindhub.todolist.controller;

import com.mindhub.todolist.configurationsjwt.JwtUtils;
import com.mindhub.todolist.controllers.user.UserEntityController;
import com.mindhub.todolist.dtos.DTOUserEntity;
import com.mindhub.todolist.models.enums.UserRoles;
import com.mindhub.todolist.services.UserEntityService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import static org.mockito.Mockito.when;


@WebMvcTest(UserEntityController.class)
@WithMockUser(username = "demo@demo.com", authorities = {"ADMIN"})
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserEntityService userEntityService;
    @MockBean
    private JwtUtils jwtUtils;

    @Test
    public void testFetchUserEntity() throws Exception {
        DTOUserEntity user = new DTOUserEntity(1, "User 1", "demo@demo.com", UserRoles.ADMIN);
        when(userEntityService.fetchByEmail("demo@demo.com")).thenReturn(user);
        mockMvc.perform(get("/api/users/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.email").value("demo@demo.com"))
                .andExpect(jsonPath("$.username").value("User 1"));
    }
}