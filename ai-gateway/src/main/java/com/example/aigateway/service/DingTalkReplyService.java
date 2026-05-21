package com.example.aigateway.service;

/**
 * 钉钉消息回复服务。
 * 通过 sessionWebhook 回复群聊消息。
 */
public interface DingTalkReplyService {

    /**
     * 回复文本消息。
     *
     * @param sessionWebhook 钉钉提供的 sessionWebhook 地址
     * @param content        回复内容
     */
    void replyText(String sessionWebhook, String content);
}
