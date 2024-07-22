package com.springboot.backendprompren.data.dto.response;

import com.springboot.backendprompren.data.entity.Competition;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseCompetitionDto {
    private Long com_id;
    private String title;
    private String content;
    private String image;
//    private Long uid;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Competition 객체를 받아서 ResponseCompetitionDto를 생성하는 생성자
    public ResponseCompetitionDto(Competition competition) {
        this.com_id = competition.getCom_id();
        this.title = competition.getTitle();
        this.content = competition.getContent();
        this.image = competition.getImage();
//        this.uid = competition.getUser().getUid();
        this.createdAt = competition.getCreatedAt();
        this.updatedAt = competition.getUpdatedAt();
    }
}
