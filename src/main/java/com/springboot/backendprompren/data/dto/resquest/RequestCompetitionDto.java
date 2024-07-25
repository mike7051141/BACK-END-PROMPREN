package com.springboot.backendprompren.data.dto.resquest;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestCompetitionDto {
    private String title;
    private String content;
    private String image;
}
