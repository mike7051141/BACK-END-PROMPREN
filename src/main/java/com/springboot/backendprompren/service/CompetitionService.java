package com.springboot.backendprompren.service;

import com.springboot.backendprompren.data.dto.response.ResponseCompetitionDto;
import com.springboot.backendprompren.data.dto.response.ResponseCompetitionListDto;
import com.springboot.backendprompren.data.dto.response.ResponsePromptListDto;
import com.springboot.backendprompren.data.dto.resquest.RequestCompetitionDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface CompetitionService {
    ResponseCompetitionDto createCompetition(RequestCompetitionDto requestDto, HttpServletRequest servletRequest) throws Exception;
    ResponseCompetitionDto getCompetition(Long com_id) throws Exception;
    ResponseCompetitionListDto getCompetitionList(int page, HttpServletRequest servletRequest);
    long countCompetitions();
    void deleteCompetition(Long com_id, HttpServletRequest servletRequest) throws Exception;
}
