package com.springboot.backendprompren.data.dto.resquest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestReviewDto {
    private Long promptId;
    private String title;
    private String content;
    private int star;

}
