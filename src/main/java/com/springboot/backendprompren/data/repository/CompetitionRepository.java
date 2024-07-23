package com.springboot.backendprompren.data.repository;

import com.springboot.backendprompren.data.entity.Competition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompetitionRepository extends JpaRepository<Competition, Long> {
    Optional<Competition> findByTitle(String title);
}
