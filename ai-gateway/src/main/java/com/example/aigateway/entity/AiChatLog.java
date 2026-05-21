package com.example.aigateway.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("ai_chat_log")
public class AiChatLog {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String source;

    @TableField("conversation_id")
    private String conversationId;

    @TableField("conversation_title")
    private String conversationTitle;

    @TableField("sender_staff_id")
    private String senderStaffId;

    @TableField("sender_nick")
    private String senderNick;

    @TableField("sender_id")
    private String senderId;

    @TableField("msg_id")
    private String msgId;

    private String question;
    private String answer;

    private Integer success;

    @TableField("error_message")
    private String errorMessage;

    @TableField("raw_request")
    private String rawRequest;

    @TableField("raw_response")
    private String rawResponse;

    @TableField("created_at")
    private LocalDateTime createdAt;
}
