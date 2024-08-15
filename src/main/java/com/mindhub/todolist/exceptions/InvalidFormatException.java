package com.mindhub.todolist.exceptions;

public class InvalidFormatException extends RuntimeException{

    public InvalidFormatException(String message) {
        super(message);
    }
}