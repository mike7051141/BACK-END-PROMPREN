package com.springboot.backendprompren.service.impl;

import com.springboot.backendprompren.config.security.JwtTokenProvider;
import com.springboot.backendprompren.data.dto.response.ResponseUserDto;
import com.springboot.backendprompren.data.entity.User;
import com.springboot.backendprompren.data.repository.UserRepository;
import com.springboot.backendprompren.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private HttpServletRequest servletRequest;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserSuccess() {
        // given
        String token = "test-token";
        String account = "test-account";

        when(jwtTokenProvider.resolveToken(servletRequest)).thenReturn(token);
        when(jwtTokenProvider.getUsername(token)).thenReturn(account);
        when(jwtTokenProvider.validationToken(token)).thenReturn(true);

        User user = new User();
        user.setUid(1L);
        user.setAccount(account);
        user.setName("Test User");
        user.setPhone("123-456-7890");
        user.setEmail("test@example.com");
        user.setNickname("testUser");
        user.setThumbnail("test-thumbnail.jpg");

        when(userRepository.getByAccount(account)).thenReturn(user);

        // when
        ResponseUserDto result = userService.getUser(servletRequest);

        // then
        assertNotNull(result);
        assertEquals(account, result.getAccount());
        assertEquals("Test User", result.getName());
        assertEquals("123-456-7890", result.getPhone());
        assertEquals("test@example.com", result.getEmail());
        assertEquals("testUser", result.getNickname());
        assertEquals("test-thumbnail.jpg", result.getThumbnail());
    }

    @Test
    void testGetUserInvalidToken() {
        // given
        String token = "invalid-token";

        when(jwtTokenProvider.resolveToken(servletRequest)).thenReturn(token);
        when(jwtTokenProvider.validationToken(token)).thenReturn(false);

        // when
        ResponseUserDto result = userService.getUser(servletRequest);

        // then
        assertNotNull(result);
        assertNull(result.getAccount());
        assertNull(result.getName());
        assertNull(result.getPhone());
        assertNull(result.getEmail());
        assertNull(result.getNickname());
        assertNull(result.getThumbnail());
    }
}
