package com.springboot.backendprompren.service;

import com.springboot.backendprompren.data.dto.response.ResponseCompetitionDto;
import com.springboot.backendprompren.data.dto.response.ResponsePromptDto;
import com.springboot.backendprompren.data.dto.resquest.RequestPromptDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface PromptService {

    ResponsePromptDto savePrompt(RequestPromptDto requestPromptDto,
                                 HttpServletRequest servletRequest,
                                 HttpServletResponse servletResponse);
    ResponsePromptDto getPrompt(Long prompt_id,
                                HttpServletRequest servletRequest,
                                HttpServletResponse servletResponse);

    List<ResponsePromptDto> getPromptByList();
    void deletePrompt(Long prompt_id,
                      HttpServletRequest servletRequest,
                      HttpServletResponse servletResponse) throws Exception;
}
