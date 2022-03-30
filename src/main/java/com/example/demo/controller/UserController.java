package com.example.demo.controller;

import com.example.demo.advice.exception.EmailSigninFailException;
import com.example.demo.advice.exception.UserNotFoundException;
import com.example.demo.config.security.JwtTokenProvider;
import com.example.demo.domain.User;
import com.example.demo.model.response.CommonResult;
import com.example.demo.model.response.ListResult;
import com.example.demo.model.response.SingleResult;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.ResponseService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@Api(tags = {"1. User"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class UserController {

    private final UserRepository userRepository;
    private final ResponseService responseService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @ApiOperation(value = "로그인", notes = "이메일 회원 로그인을 한다.")
    @PostMapping("/signin")
    public SingleResult<String> signin(@ApiParam(value = "회원ID", required = true) @RequestParam String email,
                                       @ApiParam(value = "비밀번호", required = true) @RequestParam String password) {
        User user = userRepository.findByEmail(email).orElseThrow(EmailSigninFailException::new);
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new EmailSigninFailException();
        }

        return responseService.getSingleResult(jwtTokenProvider.createToken(email, user.getRoles()));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "회원 조회", notes = "모든 회원을 조회한다.")
    @GetMapping("/user")
    public ListResult<User> findAllUser() {
        return responseService.getListResult(userRepository.findAll());
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "회원 단건 조회", notes = "userId로 회원을 조회합니다.")
    @GetMapping("/user/{userId}")
    public SingleResult<User> findUserById(@ApiParam(value = "회원ID", required = true) @PathVariable Long userId,
                                           @ApiParam(value = "언어", defaultValue = "ko") @RequestParam String lang) {
        return responseService.getSingleResult(userRepository.findById(userId).orElseThrow(UserNotFoundException::new));
    }

    @ApiOperation(value = "회원 입력", notes = "회원을 입력한다.")
    @PostMapping("/signup")
    public CommonResult save(@ApiParam(value = "회원이메일", required = true) @RequestParam String email,
                             @ApiParam(value = "비밀번호", required = true) @RequestParam String password,
                             @ApiParam(value = "회원이름", required = true) @RequestParam String name) {
        userRepository.save(User.builder()
                .email(email)
                .name(name)
                .password(passwordEncoder.encode(password))
                .roles(Collections.singletonList("USER"))
                .build());
        return responseService.getSuccessResult();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "회원 수정", notes = "회원정보를 수정한다.")
    @PutMapping("/user")
    public SingleResult<User> modify(@ApiParam(value = "회원번호", required = true) @RequestParam Long id,
                                     @ApiParam(value = "회원아이디", required = true) @RequestParam String email,
                                     @ApiParam(value = "회원이름", required = true) @RequestParam String name) {
        User user = User.builder()
                .id(id)
                .email(email)
                .name(name)
                .build();
        return responseService.getSingleResult(userRepository.save(user));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "회원 삭제", notes = "userId로 회원정보를 삭제한다.")
    @DeleteMapping("/user/{id}")
    public CommonResult delete(@ApiParam(value = "회원번호", required = true) @PathVariable Long id) {
        userRepository.deleteById(id);
        return responseService.getSuccessResult();
    }
}
