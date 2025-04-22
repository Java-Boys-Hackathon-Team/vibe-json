package ru.javaboys.vibejson.wfdefenition;

import lombok.Data;

@Data
@EntityDescription("Ветка")
public class BranchDto {
    private ConditionDto condition;
    private TransitionDto transition;
}
