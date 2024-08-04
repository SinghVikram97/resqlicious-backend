package com.vikram.resqliciousbackend.exception;

public class UserNotAuthorizedException extends RuntimeException {
    public UserNotAuthorizedException() {
        super("The user is not authorized to perform this action");
    }
}
