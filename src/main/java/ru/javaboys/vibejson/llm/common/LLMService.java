package ru.javaboys.vibejson.llm.common;

import ru.javaboys.vibejson.entity.Conversation;

public interface LLMService {
     // prompt - это одно любое сообщение от пользователя
     LLMResponseDto userPromptToWorkflow(Conversation conversation, String prompt);

     String getModelCode();
}
