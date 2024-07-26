package com.springboot.backendprompren.controller;

import com.springboot.backendprompren.config.security.JwtTokenProvider;
import com.springboot.backendprompren.data.dto.response.ResponseCompetitionDto;
import com.springboot.backendprompren.data.dto.response.ResponsePromptDto;
import com.springboot.backendprompren.data.dto.resquest.RequestCompetitionDto;
import com.springboot.backendprompren.data.dto.resquest.RequestPromptDto;
import com.springboot.backendprompren.service.PromptService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("prompt")
public class PromptController {

    private final Logger LOGGER = LoggerFactory.getLogger(PromptController.class);

    private final PromptService promptService;
    private final JwtTokenProvider jwtTokenProvider;
    @Autowired
    public PromptController(PromptService promptService, JwtTokenProvider jwtTokenProvider) {
        this.promptService = promptService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    })
    @PostMapping()
    public ResponseEntity<ResponsePromptDto> createPrompt(@RequestBody RequestPromptDto requestPromptDto,
                                                          HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws Exception{

        ResponsePromptDto savedPromptDto = promptService.savePrompt(requestPromptDto,servletRequest,servletResponse);
        return ResponseEntity.status(HttpStatus.OK).body(savedPromptDto);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    })
    @GetMapping(value = "/{prompt_id}")
    public ResponseEntity<ResponsePromptDto> getPrompt(@PathVariable Long prompt_id,
                                                       HttpServletRequest servletRequest,
                                                       HttpServletResponse servletResponse) {
        ResponsePromptDto selectedPromptDto = promptService.getPrompt(prompt_id,servletRequest,servletResponse);
        return ResponseEntity.status(HttpStatus.OK).body(selectedPromptDto);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    })
    @DeleteMapping()
    public void deletePrompt(@RequestParam(value="prompt_id",required = true)Long prompt_id,
                             HttpServletRequest servletRequest,
                             HttpServletResponse servletResponse) throws Exception {
        promptService.deletePrompt(prompt_id,servletRequest,servletResponse);
        LOGGER.info("[deleteprompt] prompt 삭제를 완료하였습니다.  , prompt id: {}", prompt_id);
    }
}
