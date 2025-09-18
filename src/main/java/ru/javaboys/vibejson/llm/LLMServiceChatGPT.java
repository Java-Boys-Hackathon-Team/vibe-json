package ru.javaboys.vibejson.llm;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.javaboys.vibejson.entity.Conversation;
import ru.javaboys.vibejson.llm.dto.LLMResponseDto;
import ru.javaboys.vibejson.llm.dto.RespDto;

@Service("Algo1")
@RequiredArgsConstructor
public class LLMServiceChatGPT implements LLMService {

    private final OpenAIService openAIService;

    @Override
    public LLMResponseDto userPromptToWorkflow(Conversation conversation, String prompt) {

        var messages = conversation.getMessages();

        var currentWorkflow = !messages.isEmpty() ? messages.get(messages.size() - 1).getJsonDslSchema().getSchemaText() : null;

        RespDto resp = openAIService.processUserMessage(conversation.getId().toString(), prompt, currentWorkflow);

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
