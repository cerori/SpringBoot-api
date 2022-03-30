package com.example.demo.advice.exception;

public class EmailSigninFailException extends RuntimeException{
    public EmailSigninFailException() {
        super();
    }

    public EmailSigninFailException(String message) {
        super(message);
    }

    public EmailSigninFailException(String message, Throwable cause) {
        super(message, cause);
    }
}
