package com.springboot.backendprompren.controller;

import com.springboot.backendprompren.data.dto.response.ResponseCompetitionDto;
import com.springboot.backendprompren.data.dto.response.ResponseCompetitionListDto;
import com.springboot.backendprompren.data.dto.request.RequestCompetitionDto;
import com.springboot.backendprompren.service.CompetitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/competition")
public class CompetitionController {

    private final CompetitionService competitionService;

    @PostMapping("/createCompetition")
    public ResponseEntity<ResponseCompetitionDto> createCompetition(@RequestBody RequestCompetitionDto requestDto, HttpServletRequest servletRequest) throws Exception {
        ResponseCompetitionDto responseDto = competitionService.createCompetition(requestDto, servletRequest);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping(value = "/getCompetition/{com_id}")
    public ResponseEntity<ResponseCompetitionDto> getPrompt(@PathVariable Long com_id) throws Exception {
        ResponseCompetitionDto selectedCompetitionDto = competitionService.getCompetition(com_id);
        return ResponseEntity.status(HttpStatus.OK).body(selectedCompetitionDto);
    }

    @GetMapping("/getCompetitionByList")
    public ResponseEntity<ResponseCompetitionListDto> getCompetitionList(@RequestParam(value = "page", required = true) int page, HttpServletRequest servletRequest) throws Exception {
        ResponseCompetitionListDto competitionList = competitionService.getCompetitionList(page - 1, servletRequest);
        return ResponseEntity.status(HttpStatus.OK).body(competitionList);
    }

    @GetMapping("/countCompetitions")
    public ResponseEntity<Long> countCompetitions() throws Exception {
        long count = competitionService.countCompetitions();
        return ResponseEntity.status(HttpStatus.OK).body(count);
    }

    @DeleteMapping("/deleteCompetitionEntry/{com_id}")
    public ResponseEntity<String> deleteCompetition(@PathVariable Long com_id, HttpServletRequest servletRequest) throws Exception {
        try {
            competitionService.deleteCompetition(com_id, servletRequest);
            return ResponseEntity.status(HttpStatus.OK).body("경진대회 삭제완료.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("경진대회 삭제실패: " + e.getMessage());
        }
    }
}
