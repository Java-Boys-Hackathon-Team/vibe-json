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

        // Последнее сообщение беседы нужно ради уже построенной схемы, но в новой
        // беседе сообщений ещё нет: обращение по индексу size() - 1 до проверки на
        // пустоту роняло первый же запрос в каждом новом чате.
        String currentWorkflow = null;
        if (messages != null && !messages.isEmpty()) {
            var jsonDslSchema = messages.get(messages.size() - 1).getJsonDslSchema();
            if (jsonDslSchema != null) {
                currentWorkflow = jsonDslSchema.getSchemaText();
            }
        }

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
