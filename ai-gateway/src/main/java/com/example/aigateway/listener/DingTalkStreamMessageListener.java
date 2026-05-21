package com.example.aigateway.listener;

import com.dingtalk.open.app.api.GenericEventListener;
import com.dingtalk.open.app.api.models.bot.ChatbotMessage;
import com.example.aigateway.dto.DingTalkBotMessageDTO;
import com.example.aigateway.service.DingTalkMessageService;
import com.example.aigateway.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 钉钉 Stream 模式机器人消息监听器。
 * <p>
 * 通过注册回调 /v1.0/im/bot/messages/get 接收机器人消息。
 * 所有消息处理逻辑委托给 DingTalkMessageService。
 * <p>
 * 事件处理成功后返回 SUCCESS ack；处理失败但不希望钉钉重复推送时也返回 SUCCESS；
 * 只有系统级异常才返回 LATER 或 RETRY。
 */
@Slf4j
@Component
public class DingTalkStreamMessageListener implements GenericEventListener<ChatbotMessage> {

    private final DingTalkMessageService dingTalkMessageService;

    public DingTalkStreamMessageListener(DingTalkMessageService dingTalkMessageService) {
        this.dingTalkMessageService = dingTalkMessageService;
    }

    @Override
    public void onEvent(ChatbotMessage event) {
        String rawJson = null;
        try {
            // 将事件对象序列化为 JSON 用于日志记录
            rawJson = JsonUtils.toJson(event);

            log.info("收到钉钉 Stream 消息事件: {}", rawJson);

            // 解析消息字段
            DingTalkBotMessageDTO message = parseMessage(event);
            if (message == null) {
                log.warn("消息解析为空，跳过处理");
                return;
            }

            dingTalkMessageService.processMessage(message, rawJson);

            log.debug("消息处理完成, msgId={}", message.getMsgId());
        } catch (Exception e) {
            log.error("消息监听器异常: {}", e.getMessage(), e);
        }
    }

    /**
     * 将 ChatbotMessage 转换为 DingTalkBotMessageDTO。
     * 兼容钉钉机器人消息结构，字段不存在时不报错。
     */
    private DingTalkBotMessageDTO parseMessage(ChatbotMessage event) {
        if (event == null) {
            return null;
        }
        DingTalkBotMessageDTO dto = new DingTalkBotMessageDTO();

        try {
            dto.setMsgId(getSafeString(event.getMsgId()));
            dto.setConversationId(getSafeString(event.getConversationId()));
            dto.setConversationTitle(getSafeString(event.getConversationTitle()));
            dto.setSenderStaffId(getSafeString(event.getSenderStaffId()));
            dto.setSenderNick(getSafeString(event.getSenderNick()));
            dto.setSenderId(getSafeString(event.getSenderId()));
            dto.setChatbotUserId(getSafeString(event.getChatbotUserId()));
            dto.setSessionWebhook(getSafeString(event.getSessionWebhook()));
            dto.setMsgtype(getSafeString(event.getMsgtype()));
            dto.setIsInAtList(getSafeString(event.getIsInAtList()));

            // 解析文本内容
            if (event.getText() != null) {
                DingTalkBotMessageDTO.Text text = new DingTalkBotMessageDTO.Text();
                text.setContent(getSafeString(event.getText().getContent()));
                dto.setText(text);
            }
        } catch (Exception e) {
            log.warn("解析消息字段时出现异常: {}", e.getMessage());
        }

        return dto;
    }

    private String getSafeString(Object obj) {
        return obj != null ? obj.toString() : null;
    }
}
