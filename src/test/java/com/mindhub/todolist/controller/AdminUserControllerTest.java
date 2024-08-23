package com.mindhub.todolist.controller;

import com.mindhub.todolist.configurationsjwt.JwtUtils;
import com.mindhub.todolist.controllers.admin.AdminUserEntityController;
import com.mindhub.todolist.dtos.DTOUserEntity;
import com.mindhub.todolist.exceptions.NotFoundException;
import com.mindhub.todolist.models.enums.UserRoles;
import com.mindhub.todolist.services.UserEntityService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@WebMvcTest(AdminUserEntityController.class)
@WithMockUser(username = "demo@demo.com", authorities = {"ADMIN"})
public class AdminUserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserEntityService userEntityService;
    @MockBean
    private JwtUtils jwtUtils;


    @Test
    public void fetchUserSuccess() throws Exception {
        DTOUserEntity dtoUserEntity = new DTOUserEntity(1, "User 1", "demo@demo.com", UserRoles.ADMIN);
        when(userEntityService.fetch("1")).thenReturn(dtoUserEntity);
        mockMvc.perform(get("/api/admin/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.username", is("User 1")))
                .andExpect(jsonPath("$.email", is("demo@demo.com")))
                .andExpect(jsonPath("$.role", is("ADMIN")));
    }

    @Test
    public void fetchUserNotFound() throws Exception {
        when(userEntityService.fetch("2")).thenThrow(new NotFoundException("Not Found User With Id: 2"));
        mockMvc.perform(get("/api/admin/users/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content().string("Not Found User With Id: 2"));
    }
}