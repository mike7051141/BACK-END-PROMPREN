package com.springboot.backendprompren.service;

import com.springboot.backendprompren.data.dto.response.ResponseCompetitionDto;
import com.springboot.backendprompren.data.dto.resquest.RequestCompetitionDto;

import java.util.ArrayList;
import java.util.List;

public interface CompetitionService {
    ResponseCompetitionDto createCompetition(RequestCompetitionDto requestDto) throws Exception;
    void deleteCompetition(Long com_id);
    List<ResponseCompetitionDto> getCompetitions();
    long countCompetitions();
}
