package com.example.aigateway.util;

public class TextUtils {

    private TextUtils() {
    }

    /**
     * 清洗用户问题：
     * 1. 去除首尾空白
     * 2. 去除连续空白
     * 3. 尝试去除 @机器人 文本
     */
    public static String cleanQuestion(String content) {
        if (content == null) {
            return "";
        }
        String result = content.trim();
        // 替换连续空白为单个空格
        result = result.replaceAll("\\s+", " ");
        // 去除 @ 开头的机器人提及，例如 "@小钉 你好" → "你好"
        result = result.replaceAll("@[\\u4e00-\\u9fa5a-zA-Z0-9_]+", "").trim();
        return result;
    }

    /**
     * 截断文本到指定长度。
     */
    public static String truncate(String text, int maxLength) {
        if (text == null) {
            return "";
        }
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength);
    }

    /**
     * 判断文本是否为空。
     */
    public static boolean isBlank(String text) {
        return text == null || text.isBlank();
    }
}
