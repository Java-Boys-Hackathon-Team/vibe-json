package ru.javaboys.vibejson.wfdefenition.dto2;

import lombok.Data;

@Data
public class BranchDto {
    private ConditionDto condition;
    private TransitionDto transition;
}
