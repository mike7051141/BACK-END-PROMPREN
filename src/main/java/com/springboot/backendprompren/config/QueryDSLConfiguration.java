package com.springboot.backendprompren.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Bean;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class QueryDSLConfiguration {
    @PersistenceContext
    EntityManager entityManager;
    @Bean
    public JPAQueryFactory jpaQueryFactory(){

        return new JPAQueryFactory(entityManager);
    }
}
