package com.springboot.backendprompren.service.impl;

import com.springboot.backendprompren.config.security.JwtTokenProvider;
import com.springboot.backendprompren.data.dto.response.ResponseCompetitionDto;
import com.springboot.backendprompren.data.dto.response.ResponseCompetitionListDto;
import com.springboot.backendprompren.data.dto.request.RequestCompetitionDto;
import com.springboot.backendprompren.data.entity.Competition;
import com.springboot.backendprompren.data.entity.User;
import com.springboot.backendprompren.data.repository.CompetitionRepository;
import com.springboot.backendprompren.data.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
    void testCreateCompetition() throws Exception {
        // given
        RequestCompetitionDto requestDto = new RequestCompetitionDto();
        requestDto.setTitle("Test Title");
        requestDto.setContent("Test Content");
        requestDto.setImage("Test Image");

        String token = "test-token";
        String account = "test-user";

        when(jwtTokenProvider.resolveToken(servletRequest)).thenReturn(token);
        when(jwtTokenProvider.getUsername(token)).thenReturn(account);

        User user = new User();
        user.setName("Test User");
        when(userRepository.getByAccount(account)).thenReturn(user);

        Competition competition = new Competition();
        competition.setUser(user);
        competition.setTitle("Test Title");
        competition.setContent("Test Content");
        competition.setImage("Test Image");
        competition.setCreatedAt(LocalDateTime.now());

        when(competitionRepository.save(any(Competition.class))).thenReturn(competition);

        // when
        ResponseCompetitionDto result = competitionService.createCompetition(requestDto, servletRequest);

        // then
        assertNotNull(result);
        assertEquals(requestDto.getTitle(), result.getTitle());
        assertEquals(user.getName(), result.getCom_writer());
    }

    @Test
    void testGetCompetition() throws Exception {
        // given
        Long com_id = 1L;
        Competition competition = new Competition();
        competition.setCom_id(com_id);
        competition.setTitle("Test Title");
        competition.setContent("Test Content");
        competition.setImage("Test Image");
        competition.setCreatedAt(LocalDateTime.now());

        User user = new User();
        user.setNickname("Test User");
        competition.setUser(user);

        when(competitionRepository.findById(com_id)).thenReturn(Optional.of(competition));

        // when
        ResponseCompetitionDto result = competitionService.getCompetition(com_id);

        // then
        assertNotNull(result);
        assertEquals("Test Title", result.getTitle());
        assertEquals("Test User", result.getCom_writer());
    }

    @Test
    void testGetCompetitionList() {
        // given
        int page = 0;
        PageRequest pageRequest = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, "createdAt"));

        // User 객체 생성
        User user = User.builder()
                .nickname("Test User")
                .build();

        // 첫 번째 Competition 객체 생성 및 User 설정
        List<Competition> competitions = new ArrayList<>();
        Competition competition1 = new Competition();
        competition1.setTitle("Test Title");
        competition1.setUser(user);
        competitions.add(competition1);

        // 두 번째 Competition 객체 생성 및 User 설정
        Competition competition2 = new Competition();
        competition2.setTitle("Test Title2");
        competition2.setUser(user);
        competitions.add(competition2);

        // Page 객체 생성 및 competitionRepository 모킹
        Page<Competition> competitionPage = new PageImpl<>(competitions, pageRequest, 2);
        when(competitionRepository.findAll(pageRequest)).thenReturn(competitionPage);

        // when
        ResponseCompetitionListDto result = competitionService.getCompetitionList(page, servletRequest);

        // then
        assertNotNull(result);
        assertEquals(2, result.getItems().size());
        assertEquals("Test Title", result.getItems().get(0).getTitle());
        assertEquals("Test Title2", result.getItems().get(1).getTitle());
        assertEquals("Test User", result.getItems().get(0).getCom_writer());
        assertEquals("Test User", result.getItems().get(1).getCom_writer());
    }

    @Test
    void testCountCompetitions() {
        // given
        when(competitionRepository.count()).thenReturn(5L);

        // when
        long count = competitionService.countCompetitions();

        // then
        assertEquals(5, count);
    }

    @Test
    void testDeleteCompetition() throws Exception {
        // given
        Long com_id = 1L;
        String token = "test-token";
        String account = "test-user";

        when(jwtTokenProvider.resolveToken(servletRequest)).thenReturn(token);
        when(jwtTokenProvider.getUsername(token)).thenReturn(account);
        when(jwtTokenProvider.validationToken(token)).thenReturn(true);

        User user = new User();
        user.setUid(1L);
        when(userRepository.getByAccount(account)).thenReturn(user);

        Competition competition = new Competition();
        competition.setUser(user);
        when(competitionRepository.findById(com_id)).thenReturn(Optional.of(competition));

        // when
        competitionService.deleteCompetition(com_id, servletRequest);

        // then
        verify(competitionRepository, times(1)).delete(competition);
    }
}
