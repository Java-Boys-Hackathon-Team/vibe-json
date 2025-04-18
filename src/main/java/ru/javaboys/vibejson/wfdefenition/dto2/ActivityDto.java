package ru.javaboys.vibejson.wfdefenition.dto2;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ActivityDto {

    @NotBlank
    private String id;

    @NotBlank
    private String type;

    private String description;

    // ID следующей activity или null
    private String transition;

    // ========================
    // Тип-зависимые поля:
    // ========================
    @Valid
    private InjectDataDto injectData;

    @Valid
    private WorkflowCallDto workflowCall;

    @Valid
    private ParallelDto parallel;

    @Valid
    private SwitchDto switchCondition;

    @Valid
    private TimerDto timer;

    // outputFilter — используется в некоторых типах
    private Object outputFilter;
}
