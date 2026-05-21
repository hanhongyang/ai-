CREATE DATABASE IF NOT EXISTS ai_gateway DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE ai_gateway;

CREATE TABLE IF NOT EXISTS ai_chat_log (
    id              BIGINT          PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    source          VARCHAR(50)     DEFAULT 'dingtalk_stream' COMMENT '来源',
    conversation_id VARCHAR(255)    COMMENT '会话ID',
    conversation_title VARCHAR(255) COMMENT '群名称',
    sender_staff_id VARCHAR(128)    COMMENT '发送人staffId',
    sender_nick     VARCHAR(128)    COMMENT '发送人昵称',
    sender_id       VARCHAR(255)    COMMENT '发送人ID',
    msg_id          VARCHAR(255)    COMMENT '钉钉消息ID',
    question        TEXT            COMMENT '用户问题',
    answer          TEXT            COMMENT 'AI回答',
    success         TINYINT         DEFAULT 1 COMMENT '是否成功',
    error_message   TEXT            COMMENT '错误信息',
    raw_request     LONGTEXT        COMMENT '钉钉原始请求',
    raw_response    LONGTEXT        COMMENT 'FastGPT原始响应',
    created_at      DATETIME        DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

    INDEX idx_conversation_id (conversation_id),
    INDEX idx_sender_staff_id (sender_staff_id),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='AI问答日志';
