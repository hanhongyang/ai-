package com.example.aigateway.service;

import com.example.aigateway.dto.DingTalkBotMessageDTO;

/**
 * 钉钉消息处理服务。
 * 负责解析钉钉机器人消息、提取问题、调用 AI、回复、记录日志。
 */
public interface DingTalkMessageService {

    /**
     * 处理钉钉机器人消息。
     *
     * @param message 解析后的钉钉消息 DTO
     * @param rawJson 原始消息 JSON（用于日志记录）
     */
    void processMessage(DingTalkBotMessageDTO message, String rawJson);
}
