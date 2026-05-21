package com.example.aigateway.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "fastgpt")
public class FastGptConfig {

    private String baseUrl;
    private String apiKey;
    private int timeoutSeconds = 30;
}
