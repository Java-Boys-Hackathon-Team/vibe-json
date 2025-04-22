package ru.javaboys.vibejson.llm.impls.kuramshin.dto;

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

