package ru.javaboys.vibejson.wfdefenition.dto2;

import lombok.Data;
import ru.javaboys.vibejson.wfdefenition.EntityDescription;

@Data
@EntityDescription("Ветка")
public class BranchDto {
    private ConditionDto condition;
    private TransitionDto transition;
}
