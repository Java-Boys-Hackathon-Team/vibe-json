package ru.javaboys.vibejson.llm.impls.kuramshin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.javaboys.vibejson.entity.Conversation;
import ru.javaboys.vibejson.llm.common.LLMResponseDto;
import ru.javaboys.vibejson.llm.common.LLMService;

@Service("Algo1")
@RequiredArgsConstructor
public class LLMServiceChatGPT implements LLMService {

    private final OpenAIService openAIService;

    @Override
    public LLMResponseDto userPromptToWorkflow(Conversation conversation, String prompt) {

        var resp = openAIService.processUserMessage(conversation.getId().toString(), prompt);

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
