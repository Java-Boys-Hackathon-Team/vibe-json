package ru.javaboys.vibejson.llm;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.javaboys.vibejson.entity.Conversation;
import ru.javaboys.vibejson.llm.dto.ChatMessageAndWorkflow;
import ru.javaboys.vibejson.llm.dto.LLMResponseDto;
import ru.javaboys.vibejson.utils.CommonUtils;

@Service("SpringAI-OpenAI")
@RequiredArgsConstructor
public class LLMServiceImpl implements LLMService {

    private final AiAgentService aiAgentService;

    @Override
    public ChatMessageAndWorkflow userPromptToWorkflow(Conversation conversation, String prompt) {

        var messages = conversation.getMessages();

        var currentWorkflow = !messages.isEmpty() ? messages.get(messages.size() - 1).getJsonDslSchema().getSchemaText() : null;

        LLMResponseDto resp = aiAgentService.processUserMessage(conversation.getId().toString(), prompt, currentWorkflow);

        return ChatMessageAndWorkflow.builder()
                .workflow(CommonUtils.toJson(resp.getWorkflow()))
                .LLMChatMsg(resp.getChatMessageForUser())
                .build();
    }

    @Override
    public String getModelCode() {
        return "SpringAI-OpenAI";
    }
}
