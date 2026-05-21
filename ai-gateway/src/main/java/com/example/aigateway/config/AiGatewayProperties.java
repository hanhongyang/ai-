package com.example.aigateway.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "dingtalk")
public class AiGatewayProperties {

    private Stream stream = new Stream();
    private Robot robot = new Robot();

    @Getter
    @Setter
    public static class Stream {
        private String clientId;
        private String clientSecret;
    }

    @Getter
    @Setter
    public static class Robot {
        private int maxQuestionLength = 500;
        private boolean enableAtCheck = true;
        private String fallbackAnswer = "抱歉，AI 助手暂时无法回答，请稍后再试。";
    }
}
