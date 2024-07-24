package com.springboot.backendprompren.service.impl;

import com.springboot.backendprompren.data.dto.CommonResponse;
import com.springboot.backendprompren.data.dto.SignDto.SignInResultDto;
import com.springboot.backendprompren.data.dto.SignDto.SignUpDto;
import com.springboot.backendprompren.data.dto.SignDto.SignUpResultDto;
import com.springboot.backendprompren.data.entity.User;
import com.springboot.backendprompren.data.repository.UserRepository;
import com.springboot.backendprompren.config.security.JwtTokenProvider;
import com.springboot.backendprompren.service.SignService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class SignServiceImpl implements SignService {

    private Logger logger = LoggerFactory.getLogger(SignServiceImpl.class);

    private JwtTokenProvider jwtTokenProvider;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public SignServiceImpl(UserRepository userRepository , JwtTokenProvider jwtTokenProvider,
                           PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;

    }

    @Override
    public SignUpResultDto SignUp(SignUpDto signUpDto, String roles) {
        User user;
        if(roles.equalsIgnoreCase("admin")){
            user = User.builder()
                    .account(signUpDto.getAccount())
                    .password(passwordEncoder.encode(signUpDto.getPassword()))
                    .name(signUpDto.getName())
                    .email(signUpDto.getEmail())
                    .phone(signUpDto.getPhone())
                    .nickname(signUpDto.getNickname())
                    .thumbnail(signUpDto.getThumbnail())
                    .roles(Collections.singletonList("ROLE_ADMIN"))
                    .build();
        }else{
            user = User.builder()
                    .account(signUpDto.getAccount())
                    .password(passwordEncoder.encode(signUpDto.getPassword()))
                    .name(signUpDto.getName())
                    .email(signUpDto.getEmail())
                    .phone(signUpDto.getPhone())
                    .nickname(signUpDto.getNickname())
                    .thumbnail(signUpDto.getThumbnail())
                    .roles(Collections.singletonList("ROLE_USER"))
                    .build();
        }
        User saveUser = userRepository.save(user);

        SignUpResultDto signUpResultDto = new SignUpResultDto();
        logger.info("[getSignResultDto] USER 정보 들어왔는지 확인 후 결과값 주입");

        if(!saveUser.getEmail().isEmpty()){
            setSucces(signUpResultDto);
        }else{
            setFail(signUpResultDto);
        }

        return signUpResultDto;
    }

    @Override
    public SignInResultDto SignIn(String account, String password) throws RuntimeException {
        logger.info("[getSignInResult] signDataHandler로 회원정보 요청");
        User user = userRepository.getByAccount(account);
        logger.info("[getSignInResult] ACCOUNT:{}",account);

        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new RuntimeException();
        }
        logger.info("[getSignInResult] 패스워드 일치");

        logger.info("[getSignInResult] SignInResultDto 객체 생성");
        SignInResultDto signInResultDto = SignInResultDto.builder()
                .token(jwtTokenProvider.createToken(String.valueOf(user.getAccount()),
                        user.getRoles()))
                .build();
        logger.info("[getSignInResult] SignInResultDto 객체에 값 주입");
        setSucces(signInResultDto);
        return signInResultDto;
    }

    private void setSucces(SignUpResultDto result){
        result.setSuccess(true);
        result.setCode(CommonResponse.SUCCESS.getCode());
        result.setMsg(CommonResponse.SUCCESS.getMsg());
    }

    private void setFail(SignUpResultDto result){
        result.setSuccess(false);
        result.setCode(CommonResponse.Fail.getCode());
        result.setMsg(CommonResponse.Fail.getMsg());

    }
}