package com.springboot.backendprompren.data.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ResponseLikedDto {
    private Long liked_id;
    private Long comId; // 경진대회 ID
    private Long uid; // 사용자 ID
    private LocalDateTime createdAt;
}