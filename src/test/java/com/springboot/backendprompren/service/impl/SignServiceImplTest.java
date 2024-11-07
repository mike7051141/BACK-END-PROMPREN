package com.springboot.backendprompren.service.impl;

import com.springboot.backendprompren.config.security.JwtTokenProvider;
import com.springboot.backendprompren.data.dto.SignDto.SignInResultDto;
import com.springboot.backendprompren.data.dto.SignDto.SignUpDto;
import com.springboot.backendprompren.data.dto.SignDto.SignUpResultDto;
import com.springboot.backendprompren.data.entity.User;
import com.springboot.backendprompren.data.repository.UserRepository;
import com.springboot.backendprompren.service.impl.SignServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SignServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private SignServiceImpl signService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSignUpUser() {
        // given
        SignUpDto signUpDto = new SignUpDto();
        signUpDto.setAccount("user1");
        signUpDto.setPassword("password");
        signUpDto.setName("User One");
        signUpDto.setEmail("user1@example.com");
        signUpDto.setPhone("123-456-7890");
        signUpDto.setNickname("userOne");
        signUpDto.setThumbnail("thumbnail.jpg");

        User user = User.builder()
                .account(signUpDto.getAccount())
                .password("encodedPassword")
                .name(signUpDto.getName())
                .email(signUpDto.getEmail())
                .phone(signUpDto.getPhone())
                .nickname(signUpDto.getNickname())
                .thumbnail(signUpDto.getThumbnail())
                .roles(Collections.singletonList("ROLE_USER"))
                .build();

        when(passwordEncoder.encode(signUpDto.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // when
        SignUpResultDto result = signService.SignUp(signUpDto, "user");

        // then
        assertNotNull(result);
        assertTrue(result.isSuccess());
    }

    @Test
    void testSignUpAdmin() {
        // given
        SignUpDto signUpDto = new SignUpDto();
        signUpDto.setAccount("admin1");
        signUpDto.setPassword("password");
        signUpDto.setName("Admin One");
        signUpDto.setEmail("admin1@example.com");
        signUpDto.setPhone("123-456-7890");
        signUpDto.setNickname("adminOne");
        signUpDto.setThumbnail("thumbnail.jpg");

        User user = User.builder()
                .account(signUpDto.getAccount())
                .password("encodedPassword")
                .name(signUpDto.getName())
                .email(signUpDto.getEmail())
                .phone(signUpDto.getPhone())
                .nickname(signUpDto.getNickname())
                .thumbnail(signUpDto.getThumbnail())
                .roles(Collections.singletonList("ROLE_ADMIN"))
                .build();

        when(passwordEncoder.encode(signUpDto.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // when
        SignUpResultDto result = signService.SignUp(signUpDto, "admin");

        // then
        assertNotNull(result);
        assertTrue(result.isSuccess());
    }

    @Test
    void testSignInSuccess() {
        // given
        String account = "user1";
        String rawPassword = "password";
        String encodedPassword = "encodedPassword";
        String token = "test-token";

        User user = new User();
        user.setAccount(account);
        user.setPassword(encodedPassword);
        user.setRoles(Collections.singletonList("ROLE_USER"));

        when(userRepository.getByAccount(account)).thenReturn(user);
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);
        when(jwtTokenProvider.createToken(account, user.getRoles())).thenReturn(token);

        // when
        SignInResultDto result = signService.SignIn(account, rawPassword);

        // then
        assertNotNull(result);
        assertEquals(token, result.getToken());
        assertTrue(result.isSuccess());
    }

    @Test
    void testSignInFailureWrongPassword() {
        // given
        String account = "user1";
        String rawPassword = "wrongPassword";
        String encodedPassword = "encodedPassword";

        User user = new User();
        user.setAccount(account);
        user.setPassword(encodedPassword);

        when(userRepository.getByAccount(account)).thenReturn(user);
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(false);

        // then
        assertThrows(RuntimeException.class, () -> signService.SignIn(account, rawPassword));
    }

    @Test
    void testSignInFailureNonExistentUser() {
        // given
        String account = "nonExistentUser";
        String password = "password";

        when(userRepository.getByAccount(account)).thenReturn(null);

        // then
        assertThrows(RuntimeException.class, () -> signService.SignIn(account, password));
    }
}
