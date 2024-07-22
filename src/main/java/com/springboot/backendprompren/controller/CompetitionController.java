package com.springboot.backendprompren.controller;

import com.springboot.backendprompren.data.dto.response.ResponseCompetitionDto;
import com.springboot.backendprompren.data.dto.resquest.RequestCompetitionDto;
import com.springboot.backendprompren.service.CompetitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/competition")
public class CompetitionController {

    @Autowired
    private final CompetitionService competitionService;

    @PostMapping("/createCompetitionEntry")
    public ResponseCompetitionDto createCompetition(@RequestBody RequestCompetitionDto requestDto) {
        return competitionService.createCompetition(requestDto);
    }

    @DeleteMapping("/deleteCompetitionEntry/{com_id}")
    public void deleteCompetition(@PathVariable Long com_id) {
        competitionService.deleteCompetition(com_id);
    }

    @GetMapping("/getCompetitionEntries")
    public List<ResponseCompetitionDto> getCompetitions() {
        return competitionService.getCompetitions();
    }

    @GetMapping("/countCompetitionEntries")
    public long countCompetitions() {
        return competitionService.countCompetitions();
    }
}
