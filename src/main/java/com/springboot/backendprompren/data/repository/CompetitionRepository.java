package com.springboot.backendprompren.data.repository;

import com.springboot.backendprompren.data.entity.Competition;
import com.springboot.backendprompren.data.entity.Prompt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompetitionRepository extends JpaRepository<Competition, Long> {
    Page<Competition> findAll(Pageable pageable);
}
