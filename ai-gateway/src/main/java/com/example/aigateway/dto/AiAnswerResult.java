package com.example.aigateway.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiAnswerResult {

    private String answer;
    private String rawResponse;
    private boolean success;
    private String errorMessage;
}
