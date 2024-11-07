package com.springboot.backendprompren.data.dto.request;

import com.springboot.backendprompren.data.entity.Condition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestPromptDto {

    private String title;
    private String category;
    private String summary;
    private String content;
    private String output;
    private String image;
    private Condition condition;

}
