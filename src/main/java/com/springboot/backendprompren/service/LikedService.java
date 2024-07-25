package com.springboot.backendprompren.service;

import com.springboot.backendprompren.data.dto.resquest.RequestLikedDto;

public interface LikedService {
    void addLiked(RequestLikedDto requestDto) throws Exception;
    void removeLiked(Long likedId);
    long countLikesForCompetition(Long comId);
}