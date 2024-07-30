package com.springboot.backendprompren.data.dto.response;

import com.springboot.backendprompren.data.entity.Condition;
import com.springboot.backendprompren.data.entity.Prompt;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponsePromptDto {
    private Long prompt_id;
    private String title;
    private String category;
    private String summary;
    private String content;
    private String output;
    private String image;
    private Condition condition;

}
