package com.springboot.backendprompren.service;

import com.springboot.backendprompren.data.dto.resquest.RequestGuideDto;

import javax.servlet.http.HttpServletRequest;

public interface GuideService {
    String evaluateText(RequestGuideDto requestGuideDto, HttpServletRequest httpServletRequest);
}
