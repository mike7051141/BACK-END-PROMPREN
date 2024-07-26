package com.springboot.backendprompren.data.dto.response;

import lombok.*;


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
    private String com_writer;
}
