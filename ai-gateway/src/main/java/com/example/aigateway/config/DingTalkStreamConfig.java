package com.example.aigateway.config;

import com.dingtalk.open.app.api.OpenDingTalkStreamClient;
import com.dingtalk.open.app.api.OpenDingTalkStreamClientBuilder;
import com.dingtalk.open.app.api.security.AuthClientCredential;
import com.example.aigateway.listener.DingTalkStreamMessageListener;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

/**
 * 钉钉 Stream 模式客户端配置。
 *
 * 服务启动后，通过 clientId/clientSecret 主动连接钉钉，
 * 无需公网回调 URL。

 * 注意：钉钉机器人接收消息的回调名称固定为 /v1.0/im/bot/messages/get。
 * 如果 SDK 支持按 topic/callbackName 注册监听器，应监听该机器人消息回调；
 * 如果 SDK 只支持通用事件监听，则在监听器中根据事件类型或消息结构判断是否为机器人消息。
 */
@Slf4j
@Configuration
public class DingTalkStreamConfig {

    private final AiGatewayProperties properties;
    private final DingTalkStreamMessageListener messageListener;

    private OpenDingTalkStreamClient client;

    public DingTalkStreamConfig(AiGatewayProperties properties,
                                DingTalkStreamMessageListener messageListener) {
        this.properties = properties;
        this.messageListener = messageListener;
    }

    @PostConstruct
    public void init() {
        String clientId = properties.getStream().getClientId();
        String clientSecret = properties.getStream().getClientSecret();

        if (clientId == null || clientId.isBlank()) {
            log.warn("DINGTALK_CLIENT_ID 未配置，钉钉 Stream 客户端将不会启动");
            return;
        }
        if (clientSecret == null || clientSecret.isBlank()) {
            log.warn("DINGTALK_CLIENT_SECRET 未配置，钉钉 Stream 客户端将不会启动");
            return;
        }

        try {
            client = OpenDingTalkStreamClientBuilder
                    .custom()
                    .credential(new AuthClientCredential(clientId, clientSecret))
                    .registerCallbackListener("/v1.0/im/bot/messages/get", messageListener)
                    .build();

            client.start();
            log.info("钉钉 Stream 客户端启动成功");
        } catch (Exception e) {
            log.error("钉钉 Stream 客户端启动失败: {}", e.getMessage(), e);
            throw new RuntimeException("钉钉 Stream 客户端启动失败", e);
        }
    }

    @PreDestroy
    public void destroy() {
        if (client != null) {
            try {
                client.stop();
                log.info("钉钉 Stream 客户端已停止");
            } catch (Exception e) {
                log.error("钉钉 Stream 客户端停止时出错: {}", e.getMessage(), e);
            }
        }
    }
}
