package com.springboot.backendprompren.service.impl;

import com.springboot.backendprompren.config.security.JwtTokenProvider;
import com.springboot.backendprompren.data.dto.response.ResponseReviewDto;
import com.springboot.backendprompren.data.dto.response.ResponseReviewListDto;
import com.springboot.backendprompren.data.dto.request.RequestReviewDto;
import com.springboot.backendprompren.data.entity.Prompt;
import com.springboot.backendprompren.data.entity.Review;
import com.springboot.backendprompren.data.entity.User;
import com.springboot.backendprompren.data.repository.PromptRepository;
import com.springboot.backendprompren.data.repository.ReviewRepository;
import com.springboot.backendprompren.data.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ReviewServiceImplTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private PromptRepository promptRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private HttpServletRequest servletRequest;

    @Mock
    private HttpServletResponse servletResponse;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveReview() {
        // given
        RequestReviewDto requestReviewDto = new RequestReviewDto();
        requestReviewDto.setTitle("Test Review");
        requestReviewDto.setContent("This is a test review content");
        requestReviewDto.setStar(5);
        requestReviewDto.setPromptId(1L);

        String token = "test-token";
        String account = "test-user";

        when(jwtTokenProvider.resolveToken(servletRequest)).thenReturn(token);
        when(jwtTokenProvider.getUsername(token)).thenReturn(account);

        User user = new User();
        user.setUid(1L);
        user.setNickname("Test User");
        user.setThumbnail("test-thumbnail.jpg");

        when(userRepository.getByAccount(account)).thenReturn(user);

        Prompt prompt = new Prompt();
        prompt.setPrompt_id(1L);
        prompt.setTitle("Test Prompt");
        prompt.setImage("test-image.jpg");

        when(promptRepository.findById(requestReviewDto.getPromptId())).thenReturn(java.util.Optional.of(prompt));

        Review review = new Review();
        review.setReview_id(1L);
        review.setUser(user);
        review.setPrompt(prompt);
        review.setTitle(requestReviewDto.getTitle());
        review.setContent(requestReviewDto.getContent());
        review.setStar(requestReviewDto.getStar());
        review.setCreatedAt(LocalDateTime.now());

        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        // when
        ResponseReviewDto result = reviewService.saveReview(requestReviewDto, servletRequest, servletResponse);

        // then
        assertNotNull(result);
        assertEquals("Test Review", result.getTitle());
        assertEquals("Test User", result.getReview_writer());
        assertEquals("Test Prompt", result.getPrompt_title());
    }

    @Test
    void testGetReviewList() {
        // given
        Long promptId = 1L;

        Prompt prompt = new Prompt();
        prompt.setPrompt_id(promptId);
        prompt.setTitle("Prompt Title");

        Review review = new Review();
        review.setTitle("Review Title");
        review.setContent("Review Content");
        review.setStar(4);
        review.setPrompt(prompt);

        User user = new User();
        user.setNickname("Review Writer");
        review.setUser(user);

        when(promptRepository.getById(promptId)).thenReturn(prompt);
        when(reviewRepository.findAllByPrompt(prompt)).thenReturn(Collections.singletonList(review));

        // when
        ResponseReviewListDto result = reviewService.getReviewList(promptId, servletRequest, servletResponse);

        // then
        assertNotNull(result);
        assertEquals(1, result.getItems().size());
        assertEquals("Review Title", result.getItems().get(0).getTitle());
        assertEquals("Review Writer", result.getItems().get(0).getReview_writer());
        assertEquals("Prompt Title", result.getItems().get(0).getPrompt_title());
    }

    @Test
    void testGetTop4ReviewList() {
        // given
        Long promptId = 1L;

        Prompt prompt = new Prompt();
        prompt.setPrompt_id(promptId);
        prompt.setTitle("Test Prompt");

        Review review = new Review();
        review.setTitle("Top Review");
        review.setContent("Top Review Content");
        review.setStar(5);
        review.setPrompt(prompt);

        User user = new User();
        user.setNickname("Top Reviewer");
        review.setUser(user);

        when(promptRepository.getById(promptId)).thenReturn(prompt);
        when(reviewRepository.findTop4ByPromptOrderByCreatedAtDesc(prompt)).thenReturn(Collections.singletonList(review));

        // when
        ResponseReviewListDto result = reviewService.getTop4ReviewList(promptId, servletRequest, servletResponse);

        // then
        assertNotNull(result);
        assertEquals(1, result.getItems().size());
        assertEquals("Top Review", result.getItems().get(0).getTitle());
        assertEquals("Top Reviewer", result.getItems().get(0).getReview_writer());
        assertEquals("Test Prompt", result.getItems().get(0).getPrompt_title());
    }

    @Test
    void testCountReviewForPrompt() {
        // given
        Long promptId = 1L;
        Prompt prompt = new Prompt();
        prompt.setPrompt_id(promptId);

        when(promptRepository.findById(promptId)).thenReturn(java.util.Optional.of(prompt));
        when(reviewRepository.countByPrompt(prompt)).thenReturn(5L);

        // when
        long count = reviewService.countReviewForPrompt(promptId, servletRequest, servletResponse);

        // then
        assertEquals(5, count);
    }
}