package com.springboot.backendprompren.service.impl;

import com.springboot.backendprompren.config.security.JwtTokenProvider;
import com.springboot.backendprompren.data.dto.response.ResponseCompetitionDto;
import com.springboot.backendprompren.data.dto.resquest.RequestCompetitionDto;
import com.springboot.backendprompren.data.entity.Competition;
import com.springboot.backendprompren.data.entity.User;
import com.springboot.backendprompren.data.repository.CompetitionRepository;
import com.springboot.backendprompren.data.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CompetitionServiceImplTest {

    @Mock
    private CompetitionRepository competitionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private HttpServletRequest servletRequest;

    @InjectMocks
    private CompetitionServiceImpl competitionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("경진대회 생성 테스트")
    void createCompetitionTest() throws Exception {
        // Arrange
        RequestCompetitionDto requestDto = RequestCompetitionDto.builder()
                .title("Title")
                .content("Content")
                .image("Image.jpg")
                .build();

        User user = User.builder()
                .account("account")
                .name("Writer")
                .build();

        Competition competition = Competition.builder()
                .user(user)
                .title("Title")
                .content("Content")
                .image("Image.jpg")
                .build();

        when(jwtTokenProvider.resolveToken(servletRequest)).thenReturn("mockedToken");
        when(jwtTokenProvider.getUsername("mockedToken")).thenReturn("account");
        when(userRepository.getByAccount("account")).thenReturn(user);
        when(competitionRepository.save(any(Competition.class))).thenReturn(competition);

        // Act
        ResponseCompetitionDto responseDto = competitionService.createCompetition(requestDto, servletRequest);

        // Assert
        assertEquals("Title", responseDto.getTitle());
        assertEquals("Content", responseDto.getContent());
        assertEquals("Image.jpg", responseDto.getImage());
        assertEquals("Writer", responseDto.getCom_writer());
    }

    @Test
    @DisplayName("경진대회 조회 테스트")
    void getCompetitionTest() throws Exception {
        // Arrange
        User user = User.builder()
                .nickname("Writer")
                .build();

        Competition competition = Competition.builder()
                .com_id(1L)
                .title("Title")
                .content("Content")
                .image("Image.jpg")
                .user(user)
                .build();

        when(competitionRepository.findById(1L)).thenReturn(Optional.of(competition));

        // Act
        ResponseCompetitionDto responseDto = competitionService.getCompetition(1L);

        // Assert
        assertEquals(1L, responseDto.getCom_id());
        assertEquals("Title", responseDto.getTitle());
        assertEquals("Content", responseDto.getContent());
        assertEquals("Image.jpg", responseDto.getImage());
        assertEquals("Writer", responseDto.getCom_writer());
    }

    @Test
    @DisplayName("경진대회 삭제 테스트 - 권한 있음")
    void deleteCompetitionTest() throws Exception {
        // Arrange
        User user = User.builder()
                .uid(1L)
                .account("account")
                .build();

        Competition competition = Competition.builder()
                .com_id(1L)
                .user(user)
                .build();

        when(jwtTokenProvider.resolveToken(servletRequest)).thenReturn("mockedToken");
        when(jwtTokenProvider.getUsername("mockedToken")).thenReturn("account");
        when(jwtTokenProvider.validationToken("mockedToken")).thenReturn(true);
        when(userRepository.getByAccount("account")).thenReturn(user);
        when(competitionRepository.findById(1L)).thenReturn(Optional.of(competition));

        // Act
        competitionService.deleteCompetition(1L, servletRequest);

        // Assert
        verify(competitionRepository, times(1)).delete(competition);
    }

    @Test
    @DisplayName("경진대회 삭제 테스트 - 권한 없음")
    void deleteCompetitionUnauthorizedTest() throws Exception {
        // Arrange
        User user = User.builder()
                .uid(1L)
                .account("account")
                .build();

        User otherUser = User.builder()
                .uid(2L)
                .account("otherAccount")
                .build();

        Competition competition = Competition.builder()
                .com_id(1L)
                .user(otherUser)
                .build();

        when(jwtTokenProvider.resolveToken(servletRequest)).thenReturn("mockedToken");
        when(jwtTokenProvider.getUsername("mockedToken")).thenReturn("account");
        when(jwtTokenProvider.validationToken("mockedToken")).thenReturn(true);
        when(userRepository.getByAccount("account")).thenReturn(user);
        when(competitionRepository.findById(1L)).thenReturn(Optional.of(competition));

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () ->
                competitionService.deleteCompetition(1L, servletRequest)
        );
        assertEquals("해당 경진대회를 삭제할 권한이 없습니다.", exception.getMessage());
        verify(competitionRepository, never()).delete(competition);
    }
}