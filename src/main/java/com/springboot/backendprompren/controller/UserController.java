package com.springboot.backendprompren.controller;

import com.springboot.backendprompren.data.dto.response.ResponseUserDto;
import com.springboot.backendprompren.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/getUser")
    public ResponseEntity<ResponseUserDto> getUser(HttpServletRequest httpServletRequest) {
        ResponseUserDto user = userService.getUser(httpServletRequest);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
}
