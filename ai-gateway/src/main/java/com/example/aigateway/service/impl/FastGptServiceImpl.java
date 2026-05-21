package com.example.aigateway.service.impl;

import com.example.aigateway.config.FastGptConfig;
import com.example.aigateway.dto.AiAnswerResult;
import com.example.aigateway.dto.FastGptChatRequest;
import com.example.aigateway.dto.FastGptChatResponse;
import com.example.aigateway.exception.AiGatewayException;
import com.example.aigateway.service.FastGptService;
import com.example.aigateway.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

@Slf4j
@Service
public class FastGptServiceImpl implements FastGptService {

    private final FastGptConfig config;
    private final HttpClient httpClient;

    public FastGptServiceImpl(FastGptConfig config) {
        this.config = config;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(config.getTimeoutSeconds()))
                .build();
    }

    @Override
    public AiAnswerResult ask(String chatId, String question) {
        // API Key 校验
        if (config.getApiKey() == null || config.getApiKey().isBlank()) {
            throw new AiGatewayException("FASTGPT_API_KEY 未配置");
        }

        FastGptChatRequest request = FastGptChatRequest.builder()
                .chatId(chatId)
                .stream(false)
                .detail(false)
                .messages(List.of(
                        FastGptChatRequest.Message.builder()
                                .role("user")
                                .content(question)
                                .build()
                ))
                .build();

        String requestBody = JsonUtils.toJson(request);

        try {
            long startTime = System.currentTimeMillis();

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(config.getBaseUrl() + "/v1/chat/completions"))
                    .header("Authorization", "Bearer " + config.getApiKey())
                    .header("Content-Type", "application/json")
                    .timeout(Duration.ofSeconds(config.getTimeoutSeconds()))
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            long cost = System.currentTimeMillis() - startTime;
            String responseBody = httpResponse.body();

            if (httpResponse.statusCode() != 200) {
                log.error("FastGPT 调用失败, statusCode={}, cost={}ms", httpResponse.statusCode(), cost);
                return AiAnswerResult.builder()
                        .success(false)
                        .rawResponse(responseBody)
                        .errorMessage("FastGPT 返回状态码: " + httpResponse.statusCode())
                        .build();
            }

            log.info("FastGPT 调用成功, cost={}ms", cost);

            FastGptChatResponse response = JsonUtils.fromJson(responseBody, FastGptChatResponse.class);
            if (response == null) {
                throw new AiGatewayException("FastGPT 响应解析失败");
            }

            if (response.getChoices() == null || response.getChoices().isEmpty()) {
                throw new AiGatewayException("FastGPT 响应中 choices 为空");
            }

            FastGptChatResponse.Choice choice = response.getChoices().get(0);
            if (choice.getMessage() == null || choice.getMessage().getContent() == null) {
                throw new AiGatewayException("FastGPT 响应中 content 为空");
            }

            String answer = choice.getMessage().getContent();

            return AiAnswerResult.builder()
                    .answer(answer)
                    .rawResponse(responseBody)
                    .success(true)
                    .build();

        } catch (AiGatewayException e) {
            throw e;
        } catch (Exception e) {
            log.error("FastGPT 调用异常: {}", e.getMessage(), e);
            return AiAnswerResult.builder()
                    .success(false)
                    .errorMessage("FastGPT 调用异常: " + e.getMessage())
                    .build();
        }
    }
}
