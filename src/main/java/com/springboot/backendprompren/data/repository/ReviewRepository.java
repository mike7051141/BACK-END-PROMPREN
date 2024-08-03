package com.springboot.backendprompren.data.repository;

import com.springboot.backendprompren.data.entity.Prompt;
import com.springboot.backendprompren.data.entity.Review;
import com.springboot.backendprompren.data.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Long> {
    long countByUserAndPrompt(User user, Prompt prompt);

    List<Review> findAllByUserAndPrompt(User user,Prompt prompt);

    //List<Review> findTop4ByUserOrderByCreatedAtDesc(User user);

    List<Review> findTop4ByUserAndPromptOrderByCreatedAtDesc(User user, Prompt prompt);
}
