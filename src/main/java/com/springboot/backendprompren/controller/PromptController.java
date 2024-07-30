package com.springboot.backendprompren.controller;


import com.springboot.backendprompren.data.dto.response.ResponsePromptDto;
import com.springboot.backendprompren.data.dto.response.ResponsePromptListDto;

import com.springboot.backendprompren.data.dto.resquest.RequestPromptDto;
import com.springboot.backendprompren.data.entity.Condition;
import com.springboot.backendprompren.service.PromptService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/v1/main")
public class PromptController {

    private final Logger LOGGER = LoggerFactory.getLogger(PromptController.class);

    private final PromptService promptService;


    @Autowired
    public PromptController(PromptService promptService) {
        this.promptService = promptService;
    }


    @PostMapping("/createPrompt")
    public ResponseEntity<ResponsePromptDto> createPrompt(@RequestBody RequestPromptDto requestPromptDto,
                                                          HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws Exception {

        ResponsePromptDto savedPromptDto = promptService.savePrompt(requestPromptDto, servletRequest, servletResponse);
        return ResponseEntity.status(HttpStatus.OK).body(savedPromptDto);
    }


    @GetMapping(value = "getPrompt/{prompt_id}")
    public ResponseEntity<ResponsePromptDto> getPrompt(@PathVariable Long prompt_id,
                                                       HttpServletRequest servletRequest,
                                                       HttpServletResponse servletResponse) {
        ResponsePromptDto selectedPromptDto = promptService.getPrompt(prompt_id, servletRequest, servletResponse);
        return ResponseEntity.status(HttpStatus.OK).body(selectedPromptDto);
    }


    @DeleteMapping("deletePrompt")
    public void deletePrompt(@RequestParam(value = "prompt_id", required = true) Long prompt_id,
                             HttpServletRequest servletRequest,
                             HttpServletResponse servletResponse) throws Exception {
        promptService.deletePrompt(prompt_id, servletRequest, servletResponse);
        LOGGER.info("[deleteprompt] prompt 삭제를 완료하였습니다.  , prompt id: {}", prompt_id);
    }

    @GetMapping("/getPromptByList")
    public ResponseEntity<ResponsePromptListDto> getPromptList(
            @RequestParam(value = "page", required = true) int page,
            @RequestParam(value = "size", required = true) int size,
            HttpServletRequest servletRequest,
            HttpServletResponse servletResponse) {
        LOGGER.info("[getToDoList] todo 리스트 조회를 시도하고 있습니다.  page : {}, size : {}"
                , page, size);

        ResponsePromptListDto promptList = promptService.getPromptList(page-1, size, servletRequest, servletResponse);

        LOGGER.info("[getPrompt] prompt 리스트 조회를 완료하였습니다. , page : {}, size : {}",
                page, size);

        return ResponseEntity.status(HttpStatus.OK).body(promptList);
    }
    @GetMapping("/getPromptByCategory")
    public Page<ResponsePromptDto> getFilteredAndSortedPrompts(@RequestParam(value = "page", required = true) int page,
                                                               @RequestParam(value = "size", required = true) int size,
                                                               @RequestParam(value = "condition",required = false) Condition condition,
                                                               @RequestParam(value = "category", required = false) String category,
                                                               HttpServletRequest servletRequest,
                                                               HttpServletResponse servletResponse){

        return promptService.getFilteredAndSortedProducts(page, size, condition, category,servletRequest,servletResponse);
    }
}

