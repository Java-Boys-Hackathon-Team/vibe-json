package ru.javaboys.vibejson.wfdefenition;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@EntityDescription("Активность")
public class ActivityDto {

    @NotBlank
    @Size(max = 255)
    private String id;

    @Size(max = 255)
    private String description;

    @Size(max = 255)
    private String transition;

    @NotBlank
    @Size(max = 255)
    private String type;

    @Valid
    private WorkflowCallDto workflowCall;

    @Valid
    private Map<String, Object> injectData;

    private Object outputFilter;

    @Valid
    private List<DataConditionDto> dataConditions;

    @Valid
    private BranchDto defaultCondition;

    @Valid
    private DefaultDataTransitionDto defaultTransition;

    @Valid
    private List<@NotBlank @Size(max = 255) String> branches;

    @Size(max = 255)
    private String completionType;

    @Size(max = 256)
    private String timerDuration;

//    @Valid
//    private TransformDto transform;//todo

}
