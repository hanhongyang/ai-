package com.example.aigateway.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 钉钉机器人消息体 DTO。
 * 兼容钉钉 Stream 模式推送的机器人消息结构。
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DingTalkBotMessageDTO {

    private String msgId;
    private String conversationId;
    private String conversationTitle;
    private String senderStaffId;
    private String senderNick;
    private String senderId;
    private String chatbotUserId;
    private String sessionWebhook;
    private String msgtype;

    private Text text;
    private String isInAtList;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Text {
        private String content;
    }

    @JsonProperty("msgId")
    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    @JsonProperty("conversationId")
    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    @JsonProperty("conversationTitle")
    public void setConversationTitle(String conversationTitle) {
        this.conversationTitle = conversationTitle;
    }

    @JsonProperty("senderStaffId")
    public void setSenderStaffId(String senderStaffId) {
        this.senderStaffId = senderStaffId;
    }

    @JsonProperty("senderNick")
    public void setSenderNick(String senderNick) {
        this.senderNick = senderNick;
    }

    @JsonProperty("senderId")
    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    @JsonProperty("chatbotUserId")
    public void setChatbotUserId(String chatbotUserId) {
        this.chatbotUserId = chatbotUserId;
    }

    @JsonProperty("sessionWebhook")
    public void setSessionWebhook(String sessionWebhook) {
        this.sessionWebhook = sessionWebhook;
    }

    @JsonProperty("msgtype")
    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    @JsonProperty("isInAtList")
    public void setIsInAtList(String isInAtList) {
        this.isInAtList = isInAtList;
    }
}
