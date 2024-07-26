package com.springboot.backendprompren.data.dto.resquest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

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

}
