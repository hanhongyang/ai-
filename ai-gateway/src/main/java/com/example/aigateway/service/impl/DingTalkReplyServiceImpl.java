package com.example.aigateway.service.impl;

import com.example.aigateway.config.AiGatewayProperties;
import com.example.aigateway.dto.DingTalkReplyRequest;
import com.example.aigateway.service.DingTalkReplyService;
import com.example.aigateway.util.JsonUtils;
import com.example.aigateway.util.TextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Slf4j
@Service
public class DingTalkReplyServiceImpl implements DingTalkReplyService {

    private static final int REPLY_TIMEOUT_SECONDS = 10;
    private static final int MAX_REPLY_LENGTH = 4000;

    private final HttpClient httpClient;
    private final String fallbackAnswer;

    public DingTalkReplyServiceImpl(AiGatewayProperties properties) {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(REPLY_TIMEOUT_SECONDS))
                .build();
        this.fallbackAnswer = properties.getRobot().getFallbackAnswer();
    }

    @Override
    public void replyText(String sessionWebhook, String content) {
        if (TextUtils.isBlank(sessionWebhook)) {
            log.error("sessionWebhook 为空，无法回复消息");
            return;
        }

        if (TextUtils.isBlank(content)) {
            content = fallbackAnswer;
        }

        // 截断超长答案，避免钉钉消息过长
        content = TextUtils.truncate(content, MAX_REPLY_LENGTH);

        DingTalkReplyRequest request = DingTalkReplyRequest.of(content);
        String body = JsonUtils.toJson(request);

        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(sessionWebhook))
                    .header("Content-Type", "application/json")
                    .timeout(Duration.ofSeconds(REPLY_TIMEOUT_SECONDS))
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                log.info("钉钉回复成功, conversationWebhook={}, statusCode={}", sessionWebhook, response.statusCode());
            } else {
                log.warn("钉钉回复失败, statusCode={}, response={}", response.statusCode(), response.body());
            }
        } catch (Exception e) {
            log.error("钉钉回复异常: {}", e.getMessage(), e);
        }
    }
}
