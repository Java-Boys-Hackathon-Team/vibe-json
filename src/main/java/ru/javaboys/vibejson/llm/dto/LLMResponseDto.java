package ru.javaboys.vibejson.llm.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import ru.javaboys.vibejson.wfdefenition.root.WorkflowDefinitionDto;

@Setter
@Getter
@Data
public class LLMResponseDto {
    private String chatMessageForUser;
    private WorkflowDefinitionDto workflow;
}

