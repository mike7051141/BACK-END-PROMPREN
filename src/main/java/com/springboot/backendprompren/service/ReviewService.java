package com.springboot.backendprompren.service;

import com.springboot.backendprompren.data.dto.response.ResponseReviewDto;
import com.springboot.backendprompren.data.dto.response.ResponseReviewListDto;
import com.springboot.backendprompren.data.dto.request.RequestReviewDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ReviewService {

    ResponseReviewDto saveReview(RequestReviewDto requestReviewDto,
                                 HttpServletRequest servletRequest,
                                 HttpServletResponse servletResponse);

    ResponseReviewListDto getReviewList(Long prompt_id,HttpServletRequest servletRequest,
                                          HttpServletResponse servletResponse);

    ResponseReviewListDto getTop4ReviewList(Long prompt_id,
                                            HttpServletRequest servletRequest,
                                            HttpServletResponse servletResponse);

    //long countReviewForPrompt(Long prompt_id);

    //long countReviewForUserAndPrompt(Long prompt_id, HttpServletRequest servletRequest,
                             // HttpServletResponse servletResponse);

    long countReviewForPrompt(Long prompt_id, HttpServletRequest servletRequest,
                              HttpServletResponse servletResponse);
}

