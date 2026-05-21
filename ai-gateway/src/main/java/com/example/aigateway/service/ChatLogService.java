package com.example.aigateway.service;

import com.example.aigateway.entity.AiChatLog;

/**
 * 问答日志保存服务。
 */
public interface ChatLogService {

    /**
     * 保存问答日志。
     *
     * @param log 日志实体
     */
    void saveLog(AiChatLog log);
}
