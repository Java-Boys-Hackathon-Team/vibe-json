package ru.javaboys.vibejson.llm.springai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class ChatGPTServiceImpl implements ChatGPTService {

    private final ChatClient chatClient;

    public ChatGPTServiceImpl(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @Override
    public String sendPrompt(String prompt) {
        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }
}
