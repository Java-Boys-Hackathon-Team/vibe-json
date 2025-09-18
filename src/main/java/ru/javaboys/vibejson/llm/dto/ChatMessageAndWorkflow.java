package ru.javaboys.vibejson.llm.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatMessageAndWorkflow {

    private String LLMChatMsg;

    private String workflow;
}
