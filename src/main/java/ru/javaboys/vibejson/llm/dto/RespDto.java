package ru.javaboys.vibejson.llm.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
public class RespDto {
    private String assistantMessage;
    private String workflowJson;
}

