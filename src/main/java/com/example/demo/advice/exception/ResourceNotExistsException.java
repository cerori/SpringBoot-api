package com.example.demo.advice.exception;

public class ResourceNotExistsException extends RuntimeException {
    public ResourceNotExistsException() {
    }

    public ResourceNotExistsException(String message) {
        super(message);
    }

    public ResourceNotExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
