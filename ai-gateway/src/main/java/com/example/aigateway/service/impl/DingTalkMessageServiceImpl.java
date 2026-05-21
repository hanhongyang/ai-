package com.example.aigateway.service.impl;

import com.example.aigateway.config.AiGatewayProperties;
import com.example.aigateway.dto.AiAnswerResult;
import com.example.aigateway.dto.DingTalkBotMessageDTO;
import com.example.aigateway.entity.AiChatLog;
import com.example.aigateway.exception.AiGatewayException;
import com.example.aigateway.service.ChatLogService;
import com.example.aigateway.service.DingTalkMessageService;
import com.example.aigateway.service.DingTalkReplyService;
import com.example.aigateway.service.FastGptService;
import com.example.aigateway.util.JsonUtils;
import com.example.aigateway.util.TextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class DingTalkMessageServiceImpl implements DingTalkMessageService {

    private final AiGatewayProperties properties;
    private final FastGptService fastGptService;
    private final DingTalkReplyService dingTalkReplyService;
    private final ChatLogService chatLogService;

    public DingTalkMessageServiceImpl(AiGatewayProperties properties,
                                      FastGptService fastGptService,
                                      DingTalkReplyService dingTalkReplyService,
                                      ChatLogService chatLogService) {
        this.properties = properties;
        this.fastGptService = fastGptService;
        this.dingTalkReplyService = dingTalkReplyService;
        this.chatLogService = chatLogService;
    }

    @Override
    public void processMessage(DingTalkBotMessageDTO message, String rawJson) {
        AiChatLog chatLog = new AiChatLog();
        chatLog.setSource("dingtalk_stream");
        chatLog.setMsgId(message.getMsgId());
        chatLog.setConversationId(message.getConversationId());
        chatLog.setConversationTitle(message.getConversationTitle());
        chatLog.setSenderStaffId(message.getSenderStaffId());
        chatLog.setSenderNick(message.getSenderNick());
        chatLog.setSenderId(message.getSenderId());
        chatLog.setRawRequest(rawJson);
        chatLog.setCreatedAt(LocalDateTime.now());

        try {
            // 只处理文本消息
            if (!"text".equals(message.getMsgtype())) {
                log.debug("跳过非文本消息, msgtype={}", message.getMsgtype());
                return;
            }

            // 提取问题文本
            String rawQuestion = message.getText() != null ? message.getText().getContent() : "";

            // @ 检查
            if (properties.getRobot().isEnableAtCheck()) {
                boolean isAt = message.getIsInAtList() != null && !message.getIsInAtList().isBlank();
                if (!isAt) {
                    log.debug("消息未 @机器人, 跳过处理");
                    return;
                }
            }

            // 清洗问题
            String question = TextUtils.cleanQuestion(rawQuestion);
            chatLog.setQuestion(question);

            log.info("收到钉钉消息: msgId={}, conversationId={}, senderNick={}, question={}",
                    message.getMsgId(), message.getConversationId(), message.getSenderNick(), question);

            if (TextUtils.isBlank(question)) {
                String reply = "请直接输入您想咨询的问题。";
                dingTalkReplyService.replyText(message.getSessionWebhook(), reply);
                chatLog.setAnswer(reply);
                chatLog.setSuccess(1);
                chatLogService.saveLog(chatLog);
                return;
            }

            // 检查问题长度
            if (question.length() > properties.getRobot().getMaxQuestionLength()) {
                String reply = "问题太长，请简化后再提问。";
                dingTalkReplyService.replyText(message.getSessionWebhook(), reply);
                chatLog.setAnswer(reply);
                chatLog.setSuccess(1);
                chatLogService.saveLog(chatLog);
                return;
            }

            // 构建 chatId
            String chatId = message.getConversationId() + "_" +
                    (message.getSenderStaffId() != null ? message.getSenderStaffId() : message.getSenderId());

            // 调用 FastGPT
            AiAnswerResult answerResult;
            try {
                answerResult = fastGptService.ask(chatId, question);
            } catch (Exception e) {
                log.error("FastGPT 调用异常: {}", e.getMessage(), e);
                answerResult = AiAnswerResult.builder()
                        .success(false)
                        .errorMessage("FastGPT 调用异常: " + e.getMessage())
                        .build();
            }

            chatLog.setRawResponse(answerResult.getRawResponse());

            if (answerResult.isSuccess()) {
                String answer = answerResult.getAnswer();
                dingTalkReplyService.replyText(message.getSessionWebhook(), answer);
                chatLog.setAnswer(answer);
                chatLog.setSuccess(1);
            } else {
                String fallback = properties.getRobot().getFallbackAnswer();
                dingTalkReplyService.replyText(message.getSessionWebhook(), fallback);
                chatLog.setAnswer(fallback);
                chatLog.setSuccess(0);
                chatLog.setErrorMessage(answerResult.getErrorMessage());
            }
        } catch (Exception e) {
            log.error("消息处理异常: {}", e.getMessage(), e);
            chatLog.setSuccess(0);
            chatLog.setErrorMessage("消息处理异常: " + e.getMessage());
            try {
                dingTalkReplyService.replyText(message.getSessionWebhook(), properties.getRobot().getFallbackAnswer());
            } catch (Exception ignored) {
                // 回复失败不影响主流程
            }
        }

        // 保存日志
        chatLogService.saveLog(chatLog);
    }
}
