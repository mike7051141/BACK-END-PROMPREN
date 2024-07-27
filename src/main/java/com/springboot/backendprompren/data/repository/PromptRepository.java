package com.springboot.backendprompren.data.repository;

import com.springboot.backendprompren.data.entity.Competition;
import com.springboot.backendprompren.data.entity.Prompt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromptRepository extends JpaRepository<Prompt,Long> {

}
