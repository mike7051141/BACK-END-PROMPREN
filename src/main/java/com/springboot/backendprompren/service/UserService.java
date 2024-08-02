package com.springboot.backendprompren.service;

import com.springboot.backendprompren.data.dto.response.ResponseUserDto;

import javax.servlet.http.HttpServletRequest;

public interface UserService {
    ResponseUserDto getUser(HttpServletRequest servletRequest);
}
