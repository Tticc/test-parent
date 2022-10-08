package com.tester.testerswing.voice.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class QywxMessageTaskDTO extends MessageTaskDTO {

    // 提示文字
    private MessageDTO message;


    public QywxMessageTaskDTO() {
    }

    public QywxMessageTaskDTO(String text) {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setTouser("WenChangYing")
                .setMsgtype("text")
                .setAgentid(1000002)
                .setText(new TextDTO().setContent(text));
        this.message = messageDTO;
    }

    @Data
    @Accessors(chain = true)
    public static class MessageDTO {
        private String touser;
        private String msgtype;
        private Integer agentid;
        private TextDTO text;
    }

    @Data
    @Accessors(chain = true)
    public static class TextDTO {
        private String content;
    }
}
