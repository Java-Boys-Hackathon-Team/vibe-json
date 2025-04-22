package ru.javaboys.vibejson.wfdefenition;

import lombok.Data;

@Data
@EntityDescription("Состояние")
public class ConditionDto {
    private String expression;
}
