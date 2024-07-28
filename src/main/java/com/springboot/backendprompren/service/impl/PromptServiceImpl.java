package com.springboot.backendprompren.service.impl;

import com.springboot.backendprompren.config.security.JwtTokenProvider;
import com.springboot.backendprompren.data.dto.response.*;
import com.springboot.backendprompren.data.dto.resquest.RequestPromptDto;
import com.springboot.backendprompren.data.entity.Prompt;
import com.springboot.backendprompren.data.entity.Review;
import com.springboot.backendprompren.data.entity.User;
import com.springboot.backendprompren.data.repository.PromptRepository;
import com.springboot.backendprompren.data.repository.UserRepository;
import com.springboot.backendprompren.service.PromptService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.List;

@Service
public class PromptServiceImpl implements PromptService {
    private final Logger LOGGER = LoggerFactory.getLogger(PromptServiceImpl.class);

    private final PromptRepository promptRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public PromptServiceImpl(PromptRepository promptRepository, UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.promptRepository = promptRepository;
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public ResponsePromptDto savePrompt(RequestPromptDto requestPromptDto,
                                        HttpServletRequest servletRequest,
                                        HttpServletResponse servletResponse) {

        String token = jwtTokenProvider.resolveToken(servletRequest);
        String account = jwtTokenProvider.getUsername(token);

        User user = userRepository.getByAccount(account);

        LOGGER.info("[savePrompt] title : {}", requestPromptDto.getTitle());

        Prompt prompt = new Prompt();
        prompt.setUser(user);
        prompt.setTitle(requestPromptDto.getTitle());
        prompt.setCategory(requestPromptDto.getCategory());
        prompt.setSummary(requestPromptDto.getSummary());
        prompt.setContent(requestPromptDto.getContent());
        prompt.setOutput(requestPromptDto.getOutput());
        prompt.setImage(requestPromptDto.getImage());

        Prompt savedPrompt = promptRepository.save(prompt);
        LOGGER.info("[savePrompt] saved PromptId : {}", savedPrompt.getPrompt_id());

        ResponsePromptDto responsePromptDto = new ResponsePromptDto();
        responsePromptDto.setPrompt_id(savedPrompt.getPrompt_id());
        responsePromptDto.setTitle(savedPrompt.getTitle());
        responsePromptDto.setCategory(savedPrompt.getCategory());
        responsePromptDto.setSummary(savedPrompt.getSummary());
        responsePromptDto.setContent(savedPrompt.getContent());
        responsePromptDto.setOutput(responsePromptDto.getOutput());
        responsePromptDto.setImage(responsePromptDto.getImage());

        LOGGER.info("[createPrompt] prompt 생성이 완료되었습니다. account : {}", account);
        return responsePromptDto;

    }

    @Override
    public ResponsePromptDto getPrompt(Long prompt_id,
                                       HttpServletRequest servletRequest,
                                       HttpServletResponse servletResponse) {
        String token = jwtTokenProvider.resolveToken(servletRequest);
        String account = jwtTokenProvider.getUsername(token);

        LOGGER.info("[getPrompt] prompt 조회를 진행합니다. account : {}", account);

        ModelMapper mapper = new ModelMapper();
        ResponsePromptDto responsePromptDto = new ResponsePromptDto();

        if(jwtTokenProvider.validationToken(token)){
            User user = userRepository.getByAccount(account);

            Prompt prompt = promptRepository.getById(prompt_id);

            if(user.getUid().equals(prompt.getUser().getUid()))
                responsePromptDto = mapper.map(prompt,ResponsePromptDto.class);
        }
        LOGGER.info("[getPrompt] prompt 조회 완료되었습니다. account : {}", account);
        return responsePromptDto;
    }

    @Override
    public ResponsePromptListDto getPromptList(int page, int size,
                                               HttpServletRequest servletRequest,
                                               HttpServletResponse servletResponse) {
        ModelMapper mapper = new ModelMapper();
        List<ResponsePromptDto> responsePromptDtoList = new ArrayList<>();
        ResponsePromptListDto responsePromptListDto = new ResponsePromptListDto();

        String token = jwtTokenProvider.resolveToken(servletRequest);

        if(jwtTokenProvider.validationToken(token)) {
            String account = jwtTokenProvider.getUsername(token);
            User user = userRepository.getByAccount(account);

            LOGGER.info("[getPromptList] prompt 조회를 진행합니다. account : {}", account);

            Page<Prompt> promptPage = promptRepository.findAll(PageRequest.of(page, size));
            List<Prompt> promptList = promptPage.getContent();

            for(Prompt prompt : promptList){
                ResponsePromptDto responsePromptDto = mapper.map(prompt, ResponsePromptDto.class);
                responsePromptDtoList.add(responsePromptDto);
            }
            responsePromptListDto.setItems(responsePromptDtoList);
            LOGGER.info("[getPromptList] Total elements: {}", promptPage.getTotalElements());
            LOGGER.info("[getPromptList] Total pages: {}", promptPage.getTotalPages());
            LOGGER.info("[getPromptList] Current page number: {}", promptPage.getNumber());
            LOGGER.info("[getPromptList] Number of elements in current page: {}", promptPage.getNumberOfElements());

            LOGGER.info("[getPromptList] prompt 조회가 완료되었습니다. account : {},{}", account);
        }
        return responsePromptListDto;

    }

    @Override
    public void deletePrompt(Long prompt_id,
                             HttpServletRequest servletRequest,
                             HttpServletResponse servletResponse) throws Exception {

        String token = jwtTokenProvider.resolveToken(servletRequest);
        String account = jwtTokenProvider.getUsername(token);

        LOGGER.info("[deleteToDo] prompt 삭재를 진행합니다. account : {}", account);

        if(jwtTokenProvider.validationToken(token)){
            User user = userRepository.getByAccount(account);
            Prompt prompt = promptRepository.getById(prompt_id);

            if(user.getUid().equals(prompt.getUser().getUid()))
                promptRepository.delete(prompt);

            LOGGER.info("[deletePrompt] prompt 삭제가 완료되었습니다. account : {}", account);
        }

    }
}
