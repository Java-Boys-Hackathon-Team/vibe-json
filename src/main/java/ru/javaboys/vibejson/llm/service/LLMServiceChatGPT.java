package ru.javaboys.vibejson.llm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.javaboys.vibejson.entity.Conversation;
import ru.javaboys.vibejson.llm.dto.LLMResponseDto;

@Service("Algo1")
@RequiredArgsConstructor
public class LLMServiceChatGPT implements LLMService {

    private final LLMServiceOpenAI llmServiceOpenAI;

    @Override
    public LLMResponseDto userPromptToWorkflow(Conversation conversation, String prompt) {

        var resp = llmServiceOpenAI.processUserMessage(conversation.getId().toString(), prompt);

        return LLMResponseDto.builder()
                .workflow(resp.getWorkflowJson())
                .LLMChatMsg(resp.getAssistantMessage())
                .build();
    }

    @Override
    public String getModelCode() {
        return "Algo1";
    }
}
