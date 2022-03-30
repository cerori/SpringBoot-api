package com.example.demo.controller;

import com.example.demo.*;
import com.example.demo.advice.UserNotFoundException;
import com.example.demo.service.ResponseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = {"1. User"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class UserController {

    private final UserRepository userRepository;
    private final ResponseService responseService;

    @ApiOperation(value = "회원 조회", notes = "모든 회원을 조회한다.")
    @GetMapping("/user")
    public ListResult<User> findAllUser() {
        return responseService.getListResult(userRepository.findAll());
    }

    @ApiOperation(value = "회원 단건 조회", notes = "userId로 회원을 조회합니다.")
    @GetMapping("/user/{userId}")
    public SingleResult<User> findUserById(@ApiParam(value = "회원ID", required = true) @PathVariable Long userId,
                                           @ApiParam(value = "언어", defaultValue = "ko") @RequestParam String lang) {
        return responseService.getSingleResult(userRepository.findById(userId).orElseThrow(UserNotFoundException::new));
    }

    @ApiOperation(value = "회원 입력", notes = "회원을 입력한다.")
    @PostMapping("/user")
    public User save(@ApiParam(value = "회원아이디", required = true) @RequestParam String uid,
                     @ApiParam(value = "회원이름", required = true) @RequestParam String name) {
        User user = User.builder()
                .uid(uid)
                .name(name)
                .build();
        return userRepository.save(user);
    }

    @ApiOperation(value = "회원 수정", notes = "회원정보를 수정한다.")
    @PutMapping("/user")
    public SingleResult<User> modify(@ApiParam(value = "회원번호", required = true) @RequestParam Long id,
                                     @ApiParam(value = "회원아이디", required = true) @RequestParam String uid,
                                     @ApiParam(value = "회원이름", required = true) @RequestParam String name) {
        User user = User.builder()
                .id(id)
                .uid(uid)
                .name(name)
                .build();
        return responseService.getSingleResult(userRepository.save(user));
    }

    @ApiOperation(value = "회원 삭제", notes = "userId로 회원정보를 삭제한다.")
    @DeleteMapping("/user/{id}")
    public CommonResult delete(@ApiParam(value = "회원번호", required = true) @PathVariable Long id) {
        userRepository.deleteById(id);
        return responseService.getSuccessResult();
    }


}
