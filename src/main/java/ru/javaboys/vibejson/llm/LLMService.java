package ru.javaboys.vibejson.llm;

import ru.javaboys.vibejson.entity.Conversation;
import ru.javaboys.vibejson.llm.dto.LLMResponseDto;

public interface LLMService {
     // prompt - это одно любое сообщение от пользователя
     LLMResponseDto userPromptToWorkflow(Conversation conversation, String prompt);

     String getModelCode();
}
