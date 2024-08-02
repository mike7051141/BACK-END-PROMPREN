package com.springboot.backendprompren.data.repository;

import com.springboot.backendprompren.data.entity.Prompt;
import com.springboot.backendprompren.data.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromptRepository extends JpaRepository<Prompt,Long> {
    Page<Prompt> findAll(Pageable pageable);


}
