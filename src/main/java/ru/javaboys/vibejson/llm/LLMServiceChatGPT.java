package ru.javaboys.vibejson.llm;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.javaboys.vibejson.entity.Conversation;
import ru.javaboys.vibejson.llm.dto.ChatMessageAndWorkflow;
import ru.javaboys.vibejson.llm.dto.RespDto;

@Service("Algo1")
@RequiredArgsConstructor
public class LLMServiceChatGPT implements LLMService {

    private final AiAgentService aiAgentService;

    @Override
    public ChatMessageAndWorkflow userPromptToWorkflow(Conversation conversation, String prompt) {

        var messages = conversation.getMessages();

        var currentWorkflow = !messages.isEmpty() ? messages.get(messages.size() - 1).getJsonDslSchema().getSchemaText() : null;

        RespDto resp = aiAgentService.processUserMessage(conversation.getId().toString(), prompt, currentWorkflow);

        return ChatMessageAndWorkflow.builder()
                .workflow(resp.getWorkflowJson())
                .LLMChatMsg(resp.getAssistantMessage())
                .build();
    }

    @Override
    public String getModelCode() {
        return "Algo1";
    }
}
