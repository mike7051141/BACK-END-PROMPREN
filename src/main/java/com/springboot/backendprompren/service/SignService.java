package com.springboot.backendprompren.service;

import com.springboot.backendprompren.data.dto.SignDto.SignInResultDto;
import com.springboot.backendprompren.data.dto.SignDto.SignUpDto;
import com.springboot.backendprompren.data.dto.SignDto.SignUpResultDto;

public interface SignService {
    SignUpResultDto SignUp(SignUpDto signUpDto, String roles);
    SignInResultDto SignIn(String account, String password) throws RuntimeException;
}
