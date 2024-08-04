package com.springboot.backendprompren.controller;


import com.springboot.backendprompren.data.dto.response.ResponseReviewDto;
import com.springboot.backendprompren.data.dto.response.ResponseReviewListDto;
import com.springboot.backendprompren.data.dto.resquest.RequestReviewDto;
import com.springboot.backendprompren.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/v1/review")
public class ReviewController {
    private final ReviewService reviewService;


    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;

    }

    @PostMapping("/createReview")
    public ResponseEntity<ResponseReviewDto> saveReview(
            @RequestBody RequestReviewDto requestReviewDto,
            HttpServletRequest servletRequest,
            HttpServletResponse servletResponse
    ) {
        ResponseReviewDto responseReviewDto = reviewService.saveReview(requestReviewDto, servletRequest, servletResponse);
        return ResponseEntity.status(HttpStatus.OK).body(responseReviewDto);
    }

    @GetMapping("/countReview/{prompt_id}")
    public ResponseEntity<String> countReveiwForPrompt(@PathVariable Long prompt_id,
                                                     HttpServletRequest servletRequest,
                                                     HttpServletResponse servletResponse) {
        try {
            Long count = reviewService.countReviewForPrompt(prompt_id, servletRequest, servletResponse);
            return ResponseEntity.status(HttpStatus.OK).body("리뷰 수: " + count);
        } catch (IllegalArgumentException e) {
            // Prompt가 존재하지 않을 때의 메시지 처리
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("프롬프트를 찾을 수 없습니다.");
        }
    }


    @GetMapping("/getReviewList/{prompt_id}")
    public ResponseEntity<ResponseReviewListDto> getReviewList( @PathVariable Long prompt_id, HttpServletRequest servletRequest,
                                                                HttpServletResponse servletResponse){
        ResponseReviewListDto reviewList= reviewService.getReviewList(prompt_id, servletRequest,servletResponse);
        return ResponseEntity.status(HttpStatus.OK).body(reviewList);
    }

    @GetMapping("/getTop4ReviewList/{prompt_id}")
    public ResponseEntity<ResponseReviewListDto> getTop4ReviewList( @PathVariable Long prompt_id, HttpServletRequest servletRequest,
                                                                    HttpServletResponse servletResponse){
        ResponseReviewListDto Top4List= reviewService.getTop4ReviewList(prompt_id, servletRequest,servletResponse);
        return ResponseEntity.status(HttpStatus.OK).body(Top4List);
    }


}