package com.mindhub.todolist.dtos;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record LoginUser(String email, String password) {

    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";

    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    public boolean validObject(){
        return !this.email.isBlank() && !this.password.isBlank();
    }

    public boolean validEmail() {
        if (this.email != null) {
            Matcher matcher = pattern.matcher(this.email);
            return matcher.find();
        }
        return false;
    }
}
