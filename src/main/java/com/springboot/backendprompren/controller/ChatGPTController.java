package com.springboot.backendprompren.controller;

import com.springboot.backendprompren.data.dto.response.ResponseChatGPT;
import com.springboot.backendprompren.data.dto.resquest.RequestChatGPT;
import com.springboot.backendprompren.data.entity.Message;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatGPTController {

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiURL;

    private final RestTemplate template;
    private static final Logger LOGGER = LoggerFactory.getLogger(ChatGPTController.class);

    @GetMapping("/api/v1/main/getGptApi")
    public String getGptApi(@RequestParam(name = "prompt") String prompt) {
        RequestChatGPT request = new RequestChatGPT(model, prompt);
        ResponseChatGPT response = template.postForObject(apiURL, request, ResponseChatGPT.class);
        return response.getChoices().get(0).getMessage().getContent();
    }

//    @GetMapping("/api/v1/main/getGptApi")
//    public String getGptApi(@RequestParam(name = "prompt") String prompt, @ApiIgnore HttpSession session) {
//        // 세션에서 이전 대화 내용 가져오기
//        List<Message> messages = (List<Message>) session.getAttribute("messages");
//        if (messages == null) {
//            messages = new ArrayList<>();
//        }
//
//        // 로그에 현재 메시지 내용 출력
//        LOGGER.info("Current messages: {}", messages);
//
//        // 현재 메시지 추가
//        messages.add(new Message("user", prompt));
//
//        // 요청 생성
//        RequestChatGPT request = new RequestChatGPT(model, messages);
//
//        // API 호출
//        ResponseChatGPT chatGPTResponse = template.postForObject(apiURL, request, ResponseChatGPT.class);
//        String responseContent = chatGPTResponse.getChoices().get(0).getMessage().getContent();
//
//        // AI 응답 메시지 추가
//        messages.add(new Message("assistant", responseContent));
//
//        // 세션에 대화 내용 저장
//        session.setAttribute("messages", messages);
//
//        return responseContent;
//    }


}
