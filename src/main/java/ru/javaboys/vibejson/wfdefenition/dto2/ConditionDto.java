package ru.javaboys.vibejson.wfdefenition.dto2;

import lombok.Data;
import ru.javaboys.vibejson.wfdefenition.EntityDescription;

@Data
@EntityDescription("Состояние")
public class ConditionDto {
    private String expression;
}
