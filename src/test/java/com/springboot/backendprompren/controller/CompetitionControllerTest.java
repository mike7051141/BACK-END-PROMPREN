package com.springboot.backendprompren.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.backendprompren.data.dto.response.ResponseCompetitionDto;
import com.springboot.backendprompren.data.dto.request.RequestCompetitionDto;
import com.springboot.backendprompren.service.CompetitionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false) // Security 필터 비활성화
@WebMvcTest(CompetitionController.class)
class CompetitionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CompetitionService competitionService;

    @Test
    @DisplayName("경진대회 생성 테스트")
    void createCompetitionTest() throws Exception {
        RequestCompetitionDto requestDto = RequestCompetitionDto.builder()
                .title("Title")
                .content("Content")
                .image("Image.jpg")
                .build();
        ResponseCompetitionDto responseDto = ResponseCompetitionDto.builder()
                .com_id(1L)
                .title("Title")
                .content("Content")
                .image("Image.jpg")
                .com_writer("Writer")
                .createdAt("2023-01-01T12:00:00")
                .build();
        // 필요한 필드 값 설정
        given(competitionService.createCompetition(any(RequestCompetitionDto.class), any()))
                .willReturn(responseDto);

        // Act - 요청 전송
        mockMvc.perform(post("/api/v1/competition/createCompetition")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))) // ObjectMapper로 JSON 변환
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.com_id").value(1L))
                .andExpect(jsonPath("$.title").value("Title"))
                .andExpect(jsonPath("$.content").value("Content"))
                .andExpect(jsonPath("$.image").value("Image.jpg"))
                .andExpect(jsonPath("$.com_writer").value("Writer"))
                .andExpect(jsonPath("$.createdAt").value("2023-01-01T12:00:00"));
    }

    @Test
    @DisplayName("경진대회 조회 테스트")
    void getCompetitionTest() throws Exception {
        ResponseCompetitionDto responseDto = ResponseCompetitionDto.builder()
                .com_id(1L)
                .title("Title")
                .content("Content")
                .image("Image.jpg")
                .com_writer("Writer")
                .createdAt("2023-01-01T12:00:00")
                .build();
        given(competitionService.getCompetition(1L)).willReturn(responseDto);

        mockMvc.perform(get("/api/v1/competition/getCompetition/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.com_id").value(1L))
                .andExpect(jsonPath("$.title").value("Title"))
                .andExpect(jsonPath("$.content").value("Content"))
                .andExpect(jsonPath("$.image").value("Image.jpg"))
                .andExpect(jsonPath("$.com_writer").value("Writer"))
                .andExpect(jsonPath("$.createdAt").value("2023-01-01T12:00:00"));
    }
}