package com.springboot.backendprompren.data.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseReviewListDto {
    private List<ResponseReviewDto> items;
}
