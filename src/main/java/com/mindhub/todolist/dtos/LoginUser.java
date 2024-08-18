package com.mindhub.todolist.dtos;

public record LoginUser(String email, String password) {
    
    
    public boolean validObject(){
        return !this.email.isBlank() && !this.password.isBlank();
    }
}
