package com.tester.testerrpc.gpt.entity.chat;


import java.util.List;

import com.tester.testerrpc.gpt.entity.billing.Usage;
import lombok.Data;

/**
 * chat答案类
 *
 * @author plexpt
 */
@Data
public class ChatCompletionResponse {
    private String id;
    private String object;
    private long created;
    private String model;
    private List<ChatChoice> choices;
    private Usage usage;
}
