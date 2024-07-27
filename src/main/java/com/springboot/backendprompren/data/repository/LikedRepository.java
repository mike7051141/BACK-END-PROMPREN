package com.springboot.backendprompren.data.repository;

import com.springboot.backendprompren.data.entity.Competition;
import com.springboot.backendprompren.data.entity.Liked;
import com.springboot.backendprompren.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikedRepository extends JpaRepository<Liked, Long> {
    Liked findByUserAndCompetition(User user, Competition competition);
    long countByCompetition(Competition competition);
}