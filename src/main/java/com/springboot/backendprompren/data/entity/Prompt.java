package com.springboot.backendprompren.data.entity;

import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="prompt")
public class Prompt extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long prompt_id;
    @Column(nullable = false, unique = true)
    private String title;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String summary;
    @Column(nullable = false)
    private String content;
    @Column(nullable = false)
    private String output;
    @Column(nullable = false)
    private String image;
    @Enumerated(EnumType.STRING)
    @Column(name = "prompt_condition", nullable = false)
    private Condition condition;

    @ManyToOne
    @JoinColumn(name="uid")
    @ToString.Exclude  //순환참조 방지
    private User user;

}
