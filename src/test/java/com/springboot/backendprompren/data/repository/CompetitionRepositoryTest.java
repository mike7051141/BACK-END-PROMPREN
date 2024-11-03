package com.springboot.backendprompren.data.repository;

import com.springboot.backendprompren.data.dto.response.ResponseCompetitionListDto;
import com.springboot.backendprompren.data.entity.Competition;
import com.springboot.backendprompren.data.entity.User;
import com.springboot.backendprompren.service.impl.CompetitionServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CompetitionRepositoryTest {

    @Mock
    private CompetitionRepository competitionRepository;

    @InjectMocks
    private CompetitionServiceImpl competitionService;

    public CompetitionRepositoryTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("경진대회 목록 조회 테스트")
    void getCompetitionListTest() {
        // Arrange
        User user = User.builder().nickname("writer123").build();

        Competition competition1 = Competition.builder()
                .title("Title1")
                .content("Content1")
                .image("Image1.jpg")
                .user(user)
                .build();

        Competition competition2 = Competition.builder()
                .title("Title2")
                .content("Content2")
                .image("Image2.jpg")
                .user(user)
                .build();

        List<Competition> competitions = List.of(competition1, competition2);
        Page<Competition> competitionPage = new PageImpl<>(competitions, PageRequest.of(0, 5, Sort.by("createdAt").descending()), competitions.size());

        when(competitionRepository.findAll(any(Pageable.class))).thenReturn(competitionPage);

        // Act
        ResponseCompetitionListDto result = competitionService.getCompetitionList(0, null);

        // Assert
        assertEquals(2, result.getItems().size());
        assertEquals("Title1", result.getItems().get(0).getTitle());
        assertEquals("writer123", result.getItems().get(0).getCom_writer());
        assertEquals("Title2", result.getItems().get(1).getTitle());
        assertEquals("writer123", result.getItems().get(1).getCom_writer());

        // Verify that the repository's findAll method was called with the correct PageRequest
        verify(competitionRepository, times(1)).findAll(any(Pageable.class));
    }
}