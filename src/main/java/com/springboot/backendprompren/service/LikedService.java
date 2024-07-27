package com.springboot.backendprompren.service;

import com.springboot.backendprompren.data.dto.resquest.RequestLikedDto;

import javax.servlet.http.HttpServletRequest;

public interface LikedService {
    boolean addLiked(RequestLikedDto requestDto, HttpServletRequest servletRequest) throws Exception;
    long countLikesForCompetition(Long com_id);
}