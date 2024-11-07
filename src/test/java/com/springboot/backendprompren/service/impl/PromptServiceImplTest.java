package com.springboot.backendprompren.service.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.springboot.backendprompren.config.security.JwtTokenProvider;
import com.springboot.backendprompren.data.dto.response.ResponsePromptDto;
import com.springboot.backendprompren.data.dto.response.ResponsePromptListDto;
import com.springboot.backendprompren.data.dto.request.RequestPromptDto;
import com.springboot.backendprompren.data.entity.Condition;
import com.springboot.backendprompren.data.entity.Prompt;
import com.springboot.backendprompren.data.entity.QPrompt;
import com.springboot.backendprompren.data.entity.User;
import com.springboot.backendprompren.data.repository.PromptRepository;
import com.springboot.backendprompren.data.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PromptServiceImplTest {

    @Mock
    private PromptRepository promptRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private JPAQueryFactory jpaQueryFactory;

    @Mock
    private HttpServletRequest servletRequest;

    @Mock
    private HttpServletResponse servletResponse;

    @InjectMocks
    private PromptServiceImpl promptService;

    private ModelMapper modelMapper = new ModelMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSavePrompt() {
        // given
        RequestPromptDto requestPromptDto = new RequestPromptDto();
        requestPromptDto.setTitle("Test Title");
        requestPromptDto.setCategory("Education");

        String token = "test-token";
        String account = "test-user";
        User user = new User();
        user.setNickname("Test User");

        when(jwtTokenProvider.resolveToken(servletRequest)).thenReturn(token);
        when(jwtTokenProvider.getUsername(token)).thenReturn(account);
        when(userRepository.getByAccount(account)).thenReturn(user);

        Prompt savedPrompt = new Prompt();
        savedPrompt.setPrompt_id(1L);
        savedPrompt.setTitle("Test Title");
        savedPrompt.setUser(user);
        savedPrompt.setCreatedAt(LocalDateTime.now());

        when(promptRepository.save(any(Prompt.class))).thenReturn(savedPrompt);

        // when
        ResponsePromptDto result = promptService.savePrompt(requestPromptDto, servletRequest, servletResponse);

        // then
        assertNotNull(result);
        assertEquals("Test Title", result.getTitle());
        assertEquals("Test User", result.getPrompt_writer());
    }

    @Test
    void testGetPrompt() {
        // given
        Long prompt_id = 1L;
        Prompt prompt = new Prompt();
        prompt.setPrompt_id(prompt_id);
        prompt.setTitle("Test Title");
        prompt.setCreatedAt(LocalDateTime.now());

        User user = new User();
        user.setNickname("Test User");
        prompt.setUser(user);

        when(promptRepository.getById(prompt_id)).thenReturn(prompt);

        // when
        ResponsePromptDto result = promptService.getPrompt(prompt_id, servletRequest, servletResponse);

        // then
        assertNotNull(result);
        assertEquals("Test Title", result.getTitle());
        assertEquals("Test User", result.getPrompt_writer());
        assertNotNull(result.getCreatedAt());
    }

    @Test
    void testGetPromptList() {
        // given
        int page = 0;
        int size = 10;
        String token = "test-token";
        String account = "test-user";

        when(jwtTokenProvider.resolveToken(servletRequest)).thenReturn(token);
        when(jwtTokenProvider.validationToken(token)).thenReturn(true);
        when(jwtTokenProvider.getUsername(token)).thenReturn(account);
        when(userRepository.getByAccount(account)).thenReturn(new User());

        Prompt prompt = new Prompt();
        prompt.setTitle("Test Title");
        Page<Prompt> promptPage = new PageImpl<>(Collections.singletonList(prompt), PageRequest.of(page, size), 1);
        when(promptRepository.findAll(PageRequest.of(page, size))).thenReturn(promptPage);

        // when
        ResponsePromptListDto result = promptService.getPromptList(page, size, servletRequest, servletResponse);

        // then
        assertNotNull(result);
        assertEquals(1, result.getItems().size());
        assertEquals("Test Title", result.getItems().get(0).getTitle());
    }

    @Test
    void testDeletePrompt() throws Exception {
        // given
        Long prompt_id = 1L;
        String token = "test-token";
        String account = "test-user";

        when(jwtTokenProvider.resolveToken(servletRequest)).thenReturn(token);
        when(jwtTokenProvider.getUsername(token)).thenReturn(account);
        when(jwtTokenProvider.validationToken(token)).thenReturn(true);

        User user = new User();
        user.setUid(1L);
        Prompt prompt = new Prompt();
        prompt.setUser(user);

        when(userRepository.getByAccount(account)).thenReturn(user);
        when(promptRepository.getById(prompt_id)).thenReturn(prompt);

        // when
        promptService.deletePrompt(prompt_id, servletRequest, servletResponse);

        // then
        verify(promptRepository, times(1)).delete(prompt);
    }

    @Test
    void testGetFilteredAndSortedProducts() {
        // given
        int page = 0;
        int size = 10;
        Condition condition = Condition.PRODUCTIVE;
        String category = "Education";
        BooleanBuilder filterBuilder = new BooleanBuilder();

        QPrompt qPrompt = QPrompt.prompt;

        // 모킹 설정: JPAQueryFactory와 selectFrom을 모킹하여 원하는 결과 반환 설정
        JPAQuery<Prompt> jpaQueryMock = mock(JPAQuery.class);
        when(jpaQueryFactory.selectFrom(qPrompt)).thenReturn(jpaQueryMock);

        // 각 메서드 체인에 대해 명확히 모킹 설정
        when(jpaQueryMock.where(any(BooleanBuilder.class))).thenReturn(jpaQueryMock);
        when(jpaQueryMock.offset(page * size)).thenReturn(jpaQueryMock);
        when(jpaQueryMock.limit(size)).thenReturn(jpaQueryMock);
        when(jpaQueryMock.orderBy(qPrompt.createdAt.desc())).thenReturn(jpaQueryMock);

        // fetch 및 fetchCount의 결과 설정
        List<Prompt> prompts = Collections.singletonList(new Prompt());
        when(jpaQueryMock.fetch()).thenReturn(prompts);
        when(jpaQueryMock.fetchCount()).thenReturn(1L);

        // when
        Page<ResponsePromptDto> result = promptService.getFilteredAndSortedProducts(page, size, condition, category, servletRequest, servletResponse);

        // then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }
}
