package com.springboot.backendprompren.data.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestLikedDto {
    private Long comId; // 경진대회 ID
}