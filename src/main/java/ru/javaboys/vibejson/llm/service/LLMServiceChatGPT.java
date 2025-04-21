package ru.javaboys.vibejson.llm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import ru.javaboys.vibejson.entity.Conversation;
import ru.javaboys.vibejson.llm.dto.LLMResponseDto;
import ru.javaboys.vibejson.llm.service.chatgpt4odeep.LLMServiceOpenAI;

@Service("lLMServiceChatGPT")
@RequiredArgsConstructor
public class LLMServiceChatGPT implements LLMService {

//    private final ChatClient chatClient;
//
//    public LLMServiceChatGPT(ChatClient.Builder chatClientBuilder) {
//        this.chatClient = chatClientBuilder.build();
//    }

    private final LLMServiceOpenAI llmServiceOpenAI;

    @Override
    public LLMResponseDto userPromptToWorkflow(Conversation conversation, String prompt) {

//        String resp = chatClient.prompt()
//                .user(prompt)
//                .call()
//                .content();
//
//        return  LLMResponseDto.builder().LLMChatMsg(resp).build();

        var resp = llmServiceOpenAI.processUserMessage(conversation.getId().toString(), prompt);

        return LLMResponseDto.builder()
                .workflow(resp.getWorkflowJson())
                .LLMChatMsg(resp.getAssistantMessage())
                .build();
    }

    @Override
    public String getModelCode() {
        return "ChatGPT";
    }
}
