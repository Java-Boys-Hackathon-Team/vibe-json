package ru.javaboys.vibejson.wfdefenition.dto2;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ActivityDto {

    @NotBlank
    @Size(max = 255)
    private String id;

    @NotBlank
    @Size(max = 255)
    private String type;

    @Size(max = 255)
    private String description;

    private String transition;

    @Valid
    private List<DataConditionDto> dataConditions;

    @Valid
    private DefaultDataTransitionDto defaultTransition;

    @Valid
    private Map<String, Object> injectData;

    @Valid
    private WorkflowCallDto workflowCall;

    private List<@NotBlank @Size(max = 255) String> branches;

    private String completionType;

    @Size(max = 256)
    private String timerDuration;

    private Object outputFilter;

    private BranchDto defaultCondition;
}
