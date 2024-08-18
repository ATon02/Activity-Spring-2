package com.mindhub.todolist.services;

import com.mindhub.todolist.dtos.LoginUser;

public interface AuthService {
    
    String login(LoginUser loginRequest);
    String register(LoginUser loginRequest);

}
