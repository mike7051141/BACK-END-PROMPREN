package com.springboot.backendprompren.service.impl;

import com.springboot.backendprompren.data.dto.response.ResponseCompetitionDto;
import com.springboot.backendprompren.data.dto.resquest.RequestCompetitionDto;
import com.springboot.backendprompren.data.entity.Competition;
import com.springboot.backendprompren.data.repository.CompetitionRepository;
import com.springboot.backendprompren.service.CompetitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompetitionServiceImpl implements CompetitionService {

    @Autowired
    private CompetitionRepository competitionRepository;

    @Override
    public ResponseCompetitionDto createCompetition(RequestCompetitionDto requestDto) {
        Competition competition = new Competition();
        competition.setTitle(requestDto.getTitle());
        competition.setContent(requestDto.getContent());
        competition.setImage(requestDto.getImage());
//        competition.setUser(new User(requestDto.getUid()));
        competition.setCreatedAt(LocalDateTime.now());
        competition.setUpdatedAt(LocalDateTime.now());

        Competition savedCompetition = competitionRepository.save(competition);
        return new ResponseCompetitionDto(savedCompetition);
    }

    @Override
    public void deleteCompetition(Long com_id) {
        competitionRepository.deleteById(com_id);

    }

    @Override
    public List<ResponseCompetitionDto> getCompetitions() {
        List<Competition> competitions = competitionRepository.findAll();
        return competitions.stream()
                .map(ResponseCompetitionDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public long countCompetitions() {
        return competitionRepository.count();
    }
}
