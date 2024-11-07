package com.springboot.backendprompren.service.impl;

import com.springboot.backendprompren.config.security.JwtTokenProvider;
import com.springboot.backendprompren.data.dto.request.RequestLikedDto;
import com.springboot.backendprompren.data.entity.Competition;
import com.springboot.backendprompren.data.entity.Liked;
import com.springboot.backendprompren.data.entity.User;
import com.springboot.backendprompren.data.repository.CompetitionRepository;
import com.springboot.backendprompren.data.repository.LikedRepository;
import com.springboot.backendprompren.data.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LikedServiceImplTest {

    @Mock
    private LikedRepository likedRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CompetitionRepository competitionRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private HttpServletRequest servletRequest;

    @InjectMocks
    private LikedServiceImpl likedService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddLikedWhenNotAlreadyLiked() throws Exception {
        // given
        RequestLikedDto requestDto = new RequestLikedDto();
        requestDto.setComId(1L);
        String token = "test-token";
        String account = "test-user";

        when(jwtTokenProvider.resolveToken(servletRequest)).thenReturn(token);
        when(jwtTokenProvider.getUsername(token)).thenReturn(account);

        User user = User.builder().account(account).build();
        when(userRepository.getByAccount(account)).thenReturn(user);

        Competition competition = new Competition();
        when(competitionRepository.findById(requestDto.getComId())).thenReturn(Optional.of(competition));

        when(likedRepository.findByUserAndCompetition(user, competition)).thenReturn(null);

        // when
        boolean result = likedService.addLiked(requestDto, servletRequest);

        // then
        assertTrue(result);
        verify(likedRepository, times(1)).save(any(Liked.class));
    }

    @Test
    void testAddLikedWhenAlreadyLiked() throws Exception {
        // given
        RequestLikedDto requestDto = new RequestLikedDto();
        requestDto.setComId(1L);
        String token = "test-token";
        String account = "test-user";

        when(jwtTokenProvider.resolveToken(servletRequest)).thenReturn(token);
        when(jwtTokenProvider.getUsername(token)).thenReturn(account);

        User user = User.builder().account(account).build();
        when(userRepository.getByAccount(account)).thenReturn(user);

        Competition competition = new Competition();
        when(competitionRepository.findById(requestDto.getComId())).thenReturn(Optional.of(competition));

        Liked existingLike = new Liked();
        existingLike.setUser(user);
        existingLike.setCompetition(competition);

        when(likedRepository.findByUserAndCompetition(user, competition)).thenReturn(existingLike);

        // when
        boolean result = likedService.addLiked(requestDto, servletRequest);

        // then
        assertFalse(result);
        verify(likedRepository, times(1)).delete(existingLike);
    }

    @Test
    void testCountLikesForCompetition() {
        // given
        Long com_id = 1L;
        Competition competition = new Competition();
        when(competitionRepository.findById(com_id)).thenReturn(Optional.of(competition));

        when(likedRepository.countByCompetition(competition)).thenReturn(10L);

        // when
        long likeCount = likedService.countLikesForCompetition(com_id);

        // then
        assertEquals(10L, likeCount);
    }
}
