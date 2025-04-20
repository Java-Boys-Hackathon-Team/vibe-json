package ru.javaboys.vibejson.llm.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import ru.javaboys.vibejson.entity.Conversation;
import ru.javaboys.vibejson.llm.dto.LLMResponseDto;

@Service("lLMServiceChatGPT")
public class LLMServiceChatGPT implements LLMService {

    private final ChatClient chatClient;

    public LLMServiceChatGPT(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @Override
    public LLMResponseDto userPromptToWorkflow(Conversation conversation, String prompt) {

        String resp = chatClient.prompt()
                .user(prompt)
                .call()
                .content();

        return  LLMResponseDto.builder().LLMChatMsg(resp).build();
    }

    @Override
    public String getModelCode() {
        return "ChatGPT";
    }
}
