package com.springboot.backendprompren.controller;

import com.springboot.backendprompren.data.dto.response.ResponseCompetitionDto;
import com.springboot.backendprompren.data.dto.resquest.RequestCompetitionDto;
import com.springboot.backendprompren.service.CompetitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/competition")
public class CompetitionController {

    @Autowired
    private final CompetitionService competitionService;

    @PostMapping("/createCompetitionEntry")
    public ResponseEntity<ResponseCompetitionDto> createCompetition(@RequestBody RequestCompetitionDto requestDto) throws Exception {
        ResponseCompetitionDto responseDto = competitionService.createCompetition(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @DeleteMapping("/deleteCompetitionEntry/{com_id}")
    public ResponseEntity<String> deleteCompetition(@PathVariable Long com_id) throws Exception {
        competitionService.deleteCompetition(com_id);
        return ResponseEntity.status(HttpStatus.OK).body("경진대회 삭제완료.");
    }

    @GetMapping("/getCompetitionEntries")
    public ResponseEntity<List<ResponseCompetitionDto>> getCompetitions() throws Exception {
        List<ResponseCompetitionDto> competitions = competitionService.getCompetitions();
        return ResponseEntity.status(HttpStatus.OK).body(competitions);
    }

    @GetMapping("/countCompetitionEntries")
    public ResponseEntity<Long> countCompetitions() throws Exception {
        long count = competitionService.countCompetitions();
        return ResponseEntity.status(HttpStatus.OK).body(count);
    }
}
