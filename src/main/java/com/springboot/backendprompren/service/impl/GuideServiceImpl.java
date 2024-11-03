package com.springboot.backendprompren.service.impl;

import com.springboot.backendprompren.data.dto.response.ResponseChatGPT;
import com.springboot.backendprompren.data.dto.resquest.RequestChatGPT;
import com.springboot.backendprompren.data.dto.resquest.RequestGuideDto;
import com.springboot.backendprompren.service.GuideService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class GuideServiceImpl implements GuideService {
    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiURL;
    @Autowired
    private RestTemplate restTemplate;
    @Override
    public String evaluateText(RequestGuideDto requestGuideDto,
                               HttpServletRequest httpServletRequest) {
        String prompt = String.format("좋은 프롬프트를 작성하기 위해 필요한 " +
                "6가지 구성요소(명령, 맥락, 페르소나, 예시, 포맷, 어조) " +
                "중 어떤 부분이 부족한지 하나만 선택하여 70자 이내로 정리하고, 이를 보완하는 방법을 제시해 주세요.\n" +
                "프롬프트: %s", requestGuideDto.getCorrection());

        RequestChatGPT request = new RequestChatGPT(model, prompt);
        ResponseChatGPT response = restTemplate.postForObject(apiURL, request, ResponseChatGPT.class);

        String gptResponse = response.getChoices().get(0).getMessage().getContent().toLowerCase();

        return gptResponse;
    }
}

