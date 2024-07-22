package com.springboot.backendprompren.data.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Competition")
public class Competition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long com_id;

    private String title;
    private String content;
    private String image;

//    @ManyToOne
//    @JoinColumn(name = "uid", nullable = false)
//    private User user;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
