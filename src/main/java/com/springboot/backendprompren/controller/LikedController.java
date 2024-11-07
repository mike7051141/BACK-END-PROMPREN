package com.springboot.backendprompren.controller;

import com.springboot.backendprompren.data.dto.request.RequestLikedDto;
import com.springboot.backendprompren.service.LikedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/like")
public class LikedController {

    private final LikedService likedService;

    @PostMapping("/createLike")
    public ResponseEntity<String> createLike(@RequestBody RequestLikedDto requestDto, HttpServletRequest servletRequest) throws Exception {
        boolean isLikedAdded = likedService.addLiked(requestDto, servletRequest);
        if (isLikedAdded) {
            return ResponseEntity.status(HttpStatus.OK).body("좋아요가 추가되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body("좋아요가 삭제되었습니다.");
        }
    }

    @GetMapping("/countLike/{com_id}")
    public ResponseEntity<Long> countLikesForCompetition(@PathVariable Long com_id) {
        long count = likedService.countLikesForCompetition(com_id);
        return ResponseEntity.status(HttpStatus.OK).body(count);
    }
}