package com.example.aigateway.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.aigateway.entity.AiChatLog;
import com.example.aigateway.mapper.AiChatLogMapper;
import com.example.aigateway.service.ChatLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ChatLogServiceImpl extends ServiceImpl<AiChatLogMapper, AiChatLog> implements ChatLogService {

    @Override
    public void saveLog(AiChatLog chatLog) {
        try {
            save(chatLog);
            log.debug("问答日志保存成功, msgId={}", chatLog.getMsgId());
        } catch (Exception e) {
            log.error("问答日志保存失败: {}", e.getMessage(), e);
        }
    }
}
