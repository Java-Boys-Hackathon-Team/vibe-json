package ru.javaboys.vibejson.llm.service;

import io.jmix.core.DataManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import ru.javaboys.vibejson.entity.ChatMessage;
import ru.javaboys.vibejson.entity.Conversation;
import ru.javaboys.vibejson.entity.SenderType;
import ru.javaboys.vibejson.llm.dto.LLMResponseDto;
import ru.javaboys.vibejson.llm.mws.client.GPTMTSClient;
import ru.javaboys.vibejson.llm.mws.dto.ChatCompletionRequestDTO;
import ru.javaboys.vibejson.llm.mws.dto.ChatCompletionResponseDTO;
import ru.javaboys.vibejson.llm.mws.dto.MessageDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

//@Service("lLMServiceMTS")
@RequiredArgsConstructor
public class LLMServiceMTS implements LLMService {

    private final GPTMTSClient gptmtsClient;
    private final DataManager dataManager;

    @Value("${mws.api.model}")
    private String model;

    @Value("${mws.api.temperature}")
    private Double temperature;

    private static final String FIND_CHAT_MESSAGES_BY_CONVERSATION_SQL = """
                select m from ChatMessage m where m.conversation.id = :conversationId order by m.createdDate
                """;

    private static String senderToRole(SenderType sender) {
        return switch (sender) {
            case USER -> "use";
            case BOT  -> "assistant";
        };
    }

    @Override
    public LLMResponseDto userPromptToWorkflow(Conversation conversation, String prompt) {

        List<ChatMessage> messages = dataManager.load(ChatMessage.class)
                .query(FIND_CHAT_MESSAGES_BY_CONVERSATION_SQL)
                .parameter("conversationId", conversation.getId())
                .list();

        List<MessageDTO> list = new ArrayList<>();
        messages.forEach(m -> list.add(
                MessageDTO.builder().role(senderToRole(m.getSenderType())).content(m.getContent()).build()));

        ChatCompletionRequestDTO req = new ChatCompletionRequestDTO();
        req.setModel(model);
        req.setTemperature(BigDecimal.valueOf(temperature));
        req.setMessages(list);

        ChatCompletionResponseDTO response = gptmtsClient.getAnswer(req);
        String answer = response.getChoices().get(0).getMessage().getContent();

        return LLMResponseDto.builder().LLMChatMsg(answer).build();
    }

    @Override
    public String getModelCode() {
        return "MTS";
    }
}
