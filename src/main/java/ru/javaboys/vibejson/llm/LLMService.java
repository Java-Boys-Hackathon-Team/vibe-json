package ru.javaboys.vibejson.llm;

import ru.javaboys.vibejson.entity.Conversation;
import ru.javaboys.vibejson.llm.dto.ChatMessageAndWorkflow;

public interface LLMService {
     // prompt - это одно любое сообщение от пользователя
     ChatMessageAndWorkflow userPromptToWorkflow(Conversation conversation, String prompt);

     String getModelCode();
}
