package ru.javaboys.vibejson.llm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.javaboys.vibejson.entity.Conversation;
import ru.javaboys.vibejson.llm.dto.LLMResponseDto;

@Service
@RequiredArgsConstructor
public class LLMServiceImpl implements LLMService {
    @Override
    public LLMResponseDto userPromptToWorkflow(Conversation conversation, String prompt) {
        return null;
    }
}
