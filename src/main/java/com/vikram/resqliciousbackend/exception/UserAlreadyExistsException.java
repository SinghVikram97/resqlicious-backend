package com.vikram.resqliciousbackend.exception;

public class UserAlreadyExistsException extends RuntimeException{
    String username;

    public UserAlreadyExistsException(String username) {
        super("User already exists with the username: "+username);
        this.username = username;
    }
}