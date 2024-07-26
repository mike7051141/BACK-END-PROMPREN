package com.springboot.backendprompren.service.impl;

import com.springboot.backendprompren.config.security.JwtTokenProvider;
import com.springboot.backendprompren.controller.PromptController;
import com.springboot.backendprompren.data.dto.response.ResponseCompetitionDto;
import com.springboot.backendprompren.data.dto.resquest.RequestCompetitionDto;
import com.springboot.backendprompren.data.entity.Competition;
import com.springboot.backendprompren.data.entity.User;
import com.springboot.backendprompren.data.repository.CompetitionRepository;
import com.springboot.backendprompren.data.repository.UserRepository;
import com.springboot.backendprompren.service.CompetitionService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompetitionServiceImpl implements CompetitionService {

    private final CompetitionRepository competitionRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    private final Logger LOGGER = LoggerFactory.getLogger(PromptController.class);

    @Override
    public ResponseCompetitionDto createCompetition(RequestCompetitionDto requestDto,
                                                    HttpServletRequest servletRequest) throws Exception {

        String token = jwtTokenProvider.resolveToken(servletRequest);
        String account = jwtTokenProvider.getUsername(token);
        User user = userRepository.getByAccount(account);

        Competition competition = new Competition();
        competition.setUser(user);
        competition.setTitle(requestDto.getTitle());
        competition.setContent(requestDto.getContent());
        competition.setImage(requestDto.getImage());

        Competition savedCompetition = competitionRepository.save(competition);

        ResponseCompetitionDto responseCompetitionDto = new ResponseCompetitionDto();
        responseCompetitionDto.setCom_id(savedCompetition.getCom_id());
        responseCompetitionDto.setTitle(savedCompetition.getTitle());
        responseCompetitionDto.setContent(savedCompetition.getContent());
        responseCompetitionDto.setImage(savedCompetition.getImage());
        responseCompetitionDto.setCom_writer(user.getName());

        LOGGER.info("[createCompetition] 경진대회 생성완료. account : {}", account);
        return responseCompetitionDto;
    }

    @Override
    public ResponseCompetitionDto getCompetition(Long com_id) throws Exception {
        LOGGER.info("[getCompetition] 경진대회 조회를 진행합니다. com_id : {}", com_id);

        Competition competition = competitionRepository.findById(com_id)
                .orElseThrow(() -> new Exception("경진대회를 찾을 수 없습니다."));

        ResponseCompetitionDto responseCompetitionDto = new ResponseCompetitionDto();
        responseCompetitionDto.setCom_id(competition.getCom_id());
        responseCompetitionDto.setTitle(competition.getTitle());
        responseCompetitionDto.setContent(competition.getContent());
        responseCompetitionDto.setImage(competition.getImage());

        // 작성자의 이름을 설정
        if (competition.getUser() != null) {
            responseCompetitionDto.setCom_writer(competition.getUser().getName());
        } else {
            responseCompetitionDto.setCom_writer(null);
        }

        LOGGER.info("[getCompetition] 경진대회 조회 완료. com_id : {}", com_id);
        return responseCompetitionDto;
    }

    @Override
    public List<ResponseCompetitionDto> getCompetitions() {
        return null;
    }

    @Override
    public long countCompetitions() {
        return competitionRepository.count();
    }

    @Override
    public void deleteCompetition(Long com_id, HttpServletRequest servletRequest) throws Exception{
        String token = jwtTokenProvider.resolveToken(servletRequest);
        String account = jwtTokenProvider.getUsername(token);

        LOGGER.info("[deleteCompetition] 경진대회 삭제를 진행합니다. account : {}", account);
        if (jwtTokenProvider.validationToken(token)) {
            User user = userRepository.getByAccount(account);
            Competition competition = competitionRepository.findById(com_id)
                    .orElseThrow(() -> new Exception("해당 경진대회를 찾을 수 없습니다."));

            LOGGER.info("user.getUid : {}",user.getUid());
            LOGGER.info("competition.getUser().getUid() : {}",competition.getUser().getUid());
            if (user.getUid().equals(competition.getUser().getUid())) {
                competitionRepository.delete(competition);
                LOGGER.info("[deleteCompetition] 경진대회 삭제완료. account : {}", account);
            }else {
                LOGGER.info("[deleteCompetition] 경진대회 삭제실패. account : {} ", account);
                throw new Exception("해당 경진대회를 삭제할 권한이 없습니다.");
            }
        }
    }
}
