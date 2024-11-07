package com.springboot.backendprompren.service;

import com.springboot.backendprompren.data.dto.response.ResponsePromptDto;
import com.springboot.backendprompren.data.dto.response.ResponsePromptListDto;
import com.springboot.backendprompren.data.dto.request.RequestPromptDto;
import com.springboot.backendprompren.data.entity.Condition;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface PromptService {

    ResponsePromptDto savePrompt(RequestPromptDto requestPromptDto,
                                 HttpServletRequest servletRequest,
                                 HttpServletResponse servletResponse);
    ResponsePromptDto getPrompt(Long prompt_id,
                                HttpServletRequest servletRequest,
                                HttpServletResponse servletResponse);

    ResponsePromptListDto getPromptList(int page, int size,
                                        HttpServletRequest servletRequest,
                                        HttpServletResponse servletResponse);
    void deletePrompt(Long prompt_id,
                      HttpServletRequest servletRequest,
                      HttpServletResponse servletResponse) throws Exception;

    Page<ResponsePromptDto> getFilteredAndSortedProducts(int page, int size, Condition condition, String category,
                                                                HttpServletRequest servletRequest,
                                                                HttpServletResponse servletResponse);
}
