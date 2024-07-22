package com.springboot.backendprompren.data.repository;

import com.springboot.backendprompren.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
        User getByAccount(String account);
}
