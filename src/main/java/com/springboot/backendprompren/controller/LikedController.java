package com.springboot.backendprompren.controller;

import com.springboot.backendprompren.data.dto.resquest.RequestLikedDto;
import com.springboot.backendprompren.service.LikedService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/like")
public class LikedController {

    @Autowired
    private LikedService likedService;

    @PostMapping("/createLike")
    public ResponseEntity<String> createLike(@RequestBody RequestLikedDto requestDto) {
        try {
            likedService.addLiked(requestDto);
            return ResponseEntity.status(HttpStatus.OK).body("Like added successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/countLike/{com_id}")
    public ResponseEntity<Long> countLikesForCompetition(@PathVariable Long com_id) {
        try {
            long count = likedService.countLikesForCompetition(com_id);
            return ResponseEntity.status(HttpStatus.OK).body(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(0L);
        }
    }

    @DeleteMapping("/dislike/{liked_id}")
    public ResponseEntity<String> dislike(@PathVariable Long liked_id) {
        try {
            likedService.removeLiked(liked_id);
            return ResponseEntity.status(HttpStatus.OK).body("Like removed successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}