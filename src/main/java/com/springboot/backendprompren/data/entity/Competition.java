package com.springboot.backendprompren.data.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Competition")
@Builder
public class Competition extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long com_id;

    @Column(unique = true, nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;
    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid")
    private User user;
}
