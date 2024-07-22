package com.springboot.backendprompren.data.repository;

import com.springboot.backendprompren.data.entity.Competition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompetitionRepository extends JpaRepository<Competition, Long> {
}
