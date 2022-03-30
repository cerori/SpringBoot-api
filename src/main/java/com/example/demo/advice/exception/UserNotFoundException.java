package com.example.demo.advice.exception;

import com.example.demo.service.ResponseService;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }

    public UserNotFoundException(String msg) {
        super(msg);
    }
    public UserNotFoundException() {
        super();
    }
}
