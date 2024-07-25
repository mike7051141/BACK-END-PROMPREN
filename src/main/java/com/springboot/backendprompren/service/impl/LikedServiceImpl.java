package com.springboot.backendprompren.service.impl;

import com.springboot.backendprompren.data.dto.resquest.RequestLikedDto;
import com.springboot.backendprompren.data.entity.Competition;
import com.springboot.backendprompren.data.entity.Liked;
import com.springboot.backendprompren.data.entity.User;
import com.springboot.backendprompren.data.repository.CompetitionRepository;
import com.springboot.backendprompren.data.repository.LikedRepository;
import com.springboot.backendprompren.data.repository.UserRepository;
import com.springboot.backendprompren.service.LikedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class LikedServiceImpl implements LikedService {

    @Autowired
    private LikedRepository likedRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompetitionRepository competitionRepository;

    @Override
    public void addLiked(RequestLikedDto requestDto) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.getByAccount(username);

        Competition competition = competitionRepository.findById(requestDto.getComId())
                .orElseThrow(() -> new IllegalArgumentException("Competition not found"));

        Optional<Liked> existingLike = likedRepository.findByUserAndCompetition(user, competition);
        if (existingLike.isPresent()) {
            // 주석 풀면 토글 느낌으로 이미 좋아요가 되어있으면 좋아요 삭제
            // likedRepository.delete(existingLike.get());
            throw new IllegalArgumentException("You have already liked this competition.");
        }

        Liked liked = new Liked();
        liked.setUser(user);
        liked.setCompetition(competition);
        liked.setCreatedAt(LocalDateTime.now());

        likedRepository.save(liked);
    }

    @Override
    public void removeLiked(Long comId) {
        // 현재 로그인한 사용자의 정보를 가져옵니다.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.getByAccount(username);

        // comId와 현재 로그인한 사용자의 정보를 사용하여 좋아요를 찾습니다.
        Optional<Liked> existingLike = likedRepository.findByUserAndCompetition(user, competitionRepository.findById(comId)
                .orElseThrow(() -> new IllegalArgumentException("Competition not found")));

        // 좋아요가 존재하면 삭제합니다.
        if (existingLike.isPresent()) {
            likedRepository.delete(existingLike.get());
        }
    }

    @Override
    public long countLikesForCompetition(Long comId) {
        return likedRepository.countByCompetitionId(comId);
    }
}