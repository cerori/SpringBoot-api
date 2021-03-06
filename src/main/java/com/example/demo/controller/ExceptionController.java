package com.example.demo.controller;

import com.example.demo.model.response.CommonResult;
import com.example.demo.advice.exception.AuthenticationEntryPointException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/exception")
public class ExceptionController {

    @GetMapping("/entrypoint")
    public CommonResult entrypointException() {
        throw new AuthenticationEntryPointException();
    }

    @GetMapping("/accessdenied")
    public CommonResult accessdeniedException() {
        throw new AccessDeniedException("");
    }
}
