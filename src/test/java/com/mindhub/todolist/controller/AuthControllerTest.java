package com.mindhub.todolist.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.web.servlet.MockMvc;

import com.mindhub.todolist.configurationsjwt.JwtUtils;
import com.mindhub.todolist.controllers.AuthController;
import com.mindhub.todolist.dtos.LoginUser;
import com.mindhub.todolist.services.AuthService;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;



@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AuthService authService;
    @MockBean
    private JwtUtils jwtUtils;

    @Test
    public void createUserSuccess() throws Exception {
        String body = "{\"email\": \"email@test.com\",\"password\": \"passwordtest\"}";
        when(authService.register(any(LoginUser.class))).thenReturn(body);
        mockMvc.perform(post("/api/auth/register")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email", is("email@test.com"))); 
        verify(authService, times(1)).register(any(LoginUser.class));
        
    }

    @Test
    public void createUserFail() throws Exception {
        String body = "{\"email\": \"demo@demo.com\",\"password\": \"passwordtest\"}";
        when(authService.register(argThat(loginUser -> "demo@demo.com".equals(loginUser.email()) 
        ))).thenThrow(new DataIntegrityViolationException("Bad request: Email is duplicated"));
        mockMvc.perform(post("/api/auth/register")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content().string("Bad request: Email is duplicated"));
        verify(authService, times(1)).register(any(LoginUser.class));
    }
}
