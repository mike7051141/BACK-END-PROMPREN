package com.springboot.backendprompren.data.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseReviewDto {
    private Long review_id;
    private String title;
    private String content;
    private String review_writer;
    private String prompt_title;
    private String writer_thumbnail;
    private String prompt_image;
    private int star;
    private String createdAt;
}
