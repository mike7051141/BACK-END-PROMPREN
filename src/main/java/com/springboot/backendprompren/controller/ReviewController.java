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
    public ResponseEntity<Long> countReveiwForPrompt(@PathVariable Long prompt_id) {
        Long count =  reviewService.countReviewForPrompt(prompt_id);
        return ResponseEntity.status(HttpStatus.OK).body(count);
    }

    @GetMapping("/getReviewList/{prompt_id}")
    public ResponseEntity<ResponseReviewListDto> getReviewList( @PathVariable Long prompt_id, HttpServletRequest servletRequest,
                                                                HttpServletResponse servletResponse){
        ResponseReviewListDto reviewList= reviewService.getReviewList(prompt_id, servletRequest,servletResponse);
        return ResponseEntity.status(HttpStatus.OK).body(reviewList);
    }

}