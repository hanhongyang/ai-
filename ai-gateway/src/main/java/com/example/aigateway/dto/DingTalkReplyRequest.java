package com.example.aigateway.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DingTalkReplyRequest {

    private String msgtype = "text";
    private Text text;

    public static DingTalkReplyRequest of(String content) {
        DingTalkReplyRequest req = new DingTalkReplyRequest();
        req.text = new Text(content);
        return req;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Text {
        private String content;
    }
}
