package com.springboot.backendprompren.service.impl;

import com.springboot.backendprompren.config.security.JwtTokenProvider;
import com.springboot.backendprompren.controller.LikedController;
import com.springboot.backendprompren.data.dto.resquest.RequestLikedDto;
import com.springboot.backendprompren.data.entity.Competition;
import com.springboot.backendprompren.data.entity.Liked;
import com.springboot.backendprompren.data.entity.User;
import com.springboot.backendprompren.data.repository.CompetitionRepository;
import com.springboot.backendprompren.data.repository.LikedRepository;
import com.springboot.backendprompren.data.repository.UserRepository;
import com.springboot.backendprompren.service.LikedService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikedServiceImpl implements LikedService {

    private final LikedRepository likedRepository;
    private final UserRepository userRepository;
    private final CompetitionRepository competitionRepository;

    private final JwtTokenProvider jwtTokenProvider;
    private final Logger LOGGER = LoggerFactory.getLogger(LikedController.class);

    @Override
    public boolean addLiked(RequestLikedDto requestDto, HttpServletRequest servletRequest) throws Exception {
        String token = jwtTokenProvider.resolveToken(servletRequest);
        String account = jwtTokenProvider.getUsername(token);
        User user = userRepository.getByAccount(account);

        Competition competition = competitionRepository.findById(requestDto.getComId())
                .orElseThrow(() -> new IllegalArgumentException("경진대회를 찾을 수 없습니다."));

        Liked existingLike = likedRepository.findByUserAndCompetition(user, competition);
        if (existingLike != null) {
            // 이미 좋아요가 되어있으면 좋아요 삭제
            likedRepository.delete(existingLike);
            LOGGER.info("[deleteLiked] 좋아요가 삭제되었습니다. account : {}", account);
            return false;
        } else {
            // 좋아요 추가
            Liked liked = new Liked();
            liked.setUser(user);
            liked.setCompetition(competition);
            likedRepository.save(liked);
            LOGGER.info("[addLiked] 좋아요가 추가되었습니다. account : {}", account);
            return true;
        }
    }

    @Override
    public long countLikesForCompetition(Long com_id) {
        Competition competition = competitionRepository.findById(com_id)
                .orElseThrow(() -> new IllegalArgumentException("경진대회를 찾을 수 없습니다."));
        return likedRepository.countByCompetition(competition);
    }
}