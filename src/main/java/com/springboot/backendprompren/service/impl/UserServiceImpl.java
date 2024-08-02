package com.springboot.backendprompren.service.impl;

import com.springboot.backendprompren.config.security.JwtTokenProvider;
import com.springboot.backendprompren.controller.PromptController;
import com.springboot.backendprompren.data.dto.response.ResponseUserDto;
import com.springboot.backendprompren.data.entity.Competition;
import com.springboot.backendprompren.data.entity.User;
import com.springboot.backendprompren.data.repository.CompetitionRepository;
import com.springboot.backendprompren.data.repository.UserRepository;
import com.springboot.backendprompren.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    private final Logger LOGGER = LoggerFactory.getLogger(PromptController.class);
    @Override
    public ResponseUserDto getUser(HttpServletRequest servletRequest) {
        String token = jwtTokenProvider.resolveToken(servletRequest);
        String account = jwtTokenProvider.getUsername(token);

        ResponseUserDto responseUserDto = new ResponseUserDto();
        if (jwtTokenProvider.validationToken(token)) {
            User user = userRepository.getByAccount(account);
            responseUserDto.setAccount(user.getAccount());
            responseUserDto.setName(user.getName());
            responseUserDto.setPhone(user.getPhone());
            responseUserDto.setEmail(user.getEmail());
            responseUserDto.setUid(user.getUid());
            responseUserDto.setNickname(user.getNickname());
            responseUserDto.setThumbnail(user.getThumbnail());
            LOGGER.info("[getUser] 유저 불러오기 성공. responseUserDto : {} ", responseUserDto);
        }
        return responseUserDto;
    }
}
