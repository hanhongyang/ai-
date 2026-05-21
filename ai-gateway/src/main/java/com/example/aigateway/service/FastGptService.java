package com.example.aigateway.service;

import com.example.aigateway.dto.AiAnswerResult;

/**
 * FastGPT AI 调用服务。
 * 封装对 FastGPT API 的调用。
 */
public interface FastGptService {

    /**
     * 向 FastGPT 提问并获取回答。
     *
     * @param chatId  会话 ID，用于上下文关联
     * @param question 用户问题
     * @return AI 回答结果
     */
    AiAnswerResult ask(String chatId, String question);
}
