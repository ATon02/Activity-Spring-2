package com.mindhub.todolist.controller;

import com.mindhub.todolist.configurationsjwt.JwtUtils;
import com.mindhub.todolist.controllers.admin.AdminTaskController;
import com.mindhub.todolist.dtos.DTOTask;
import com.mindhub.todolist.exceptions.NotFoundException;
import com.mindhub.todolist.models.enums.TaskStatus;
import com.mindhub.todolist.services.AuthService;
import com.mindhub.todolist.services.TaskService;
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


@WebMvcTest(AdminTaskController.class)
@WithMockUser(username = "demo@demo.com", authorities = {"ADMIN"}, password = "020202020")
public class AdminTaskControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TaskService taskService;
    @MockBean
    private JwtUtils jwtUtils;
    @MockBean
    private AuthService authService;


    @Test
    public void fetchTaskSuccess() throws Exception {
        DTOTask dtoTask = new DTOTask(1, "title test", "Task Description", TaskStatus.PENDING);
        when(taskService.fetch("1")).thenReturn(dtoTask);
        mockMvc.perform(get("/api/admin/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("title test")))
                .andExpect(jsonPath("$.description", is("Task Description")));
    }

    @Test
    public void fetchTaskNotFound() throws Exception {
        when(taskService.fetch("2")).thenThrow(new NotFoundException("Not Found Task With Id: 2"));
        mockMvc.perform(get("/api/admin/tasks/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content().string("Not Found Task With Id: 2"));
    }


}

