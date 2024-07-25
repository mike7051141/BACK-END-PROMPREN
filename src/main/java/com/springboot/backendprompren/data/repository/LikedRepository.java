package com.springboot.backendprompren.data.repository;

import com.springboot.backendprompren.data.entity.Competition;
import com.springboot.backendprompren.data.entity.Liked;
import com.springboot.backendprompren.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LikedRepository extends JpaRepository<Liked, Long> {
    Optional<Liked> findByUserAndCompetition(User user, Competition competition);

    @Query("SELECT COUNT(l) FROM Liked l WHERE l.competition.com_id = :comId")
    long countByCompetitionId(@Param("comId") Long comId);
}