package ru.javaboys.vibejson.llm;

import io.jmix.core.DataManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.javaboys.vibejson.entity.Conversation;
import ru.javaboys.vibejson.entity.JsonDslSchema;
import ru.javaboys.vibejson.llm.dto.ChatMessageAndWorkflow;
import ru.javaboys.vibejson.llm.dto.LLMResponseDto;
import ru.javaboys.vibejson.utils.CommonUtils;

@Service("SpringAI-OpenAI")
@RequiredArgsConstructor
public class LLMServiceImpl implements LLMService {

    private final AiAgentService aiAgentService;
    private final DataManager dataManager;

    @Override
    public ChatMessageAndWorkflow userPromptToWorkflow(Conversation conversation, String prompt) {
        LLMResponseDto resp = aiAgentService.processUserMessage(
                conversation.getId().toString(), prompt, findLatestWorkflow(conversation));

        return ChatMessageAndWorkflow.builder()
                .workflow(CommonUtils.toJson(resp.getWorkflow()))
                .LLMChatMsg(resp.getChatMessageForUser())
                .build();
    }

    /**
     * Последняя схема беседы, с учётом ручных правок.
     * <p>
     * Раньше схема бралась из последнего сообщения беседы, но к моменту вызова
     * последним всегда оказывается только что сохранённый вопрос пользователя,
     * у которого схемы нет. В итоге модель не видела текущую схему и не могла
     * аккуратно её доработать.
     */
    private String findLatestWorkflow(Conversation conversation) {
        return dataManager.load(JsonDslSchema.class)
                .query("""
                        select s from JsonDslSchema s
                        where s.chatMessage.conversation.id = :conversationId
                        order by s.createdDate desc""")
                .parameter("conversationId", conversation.getId())
                .maxResults(1)
                .optional()
                .map(JsonDslSchema::getSchemaText)
                .orElse(null);
    }

    @Override
    public String getModelCode() {
        return "SpringAI-OpenAI";
    }
}
