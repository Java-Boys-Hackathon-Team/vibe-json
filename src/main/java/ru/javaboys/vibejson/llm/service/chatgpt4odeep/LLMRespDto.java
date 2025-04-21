package ru.javaboys.vibejson.llm.service.chatgpt4odeep;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
public class LLMRespDto {
    private String assistantMessage;
    private String workflowJson;
}

