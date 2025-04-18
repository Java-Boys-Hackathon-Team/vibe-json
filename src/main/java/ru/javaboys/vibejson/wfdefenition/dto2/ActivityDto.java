package ru.javaboys.vibejson.wfdefenition.dto2;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

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

    // inject
    @Valid
    private InjectDataDto injectData;

    // workflow_call
    @Valid
    private WorkflowCallDto workflowCall;

    // switch
    @Valid
    private SwitchDto switchCondition;

    // parallel
    private List<@NotBlank @Size(max = 255) String> branches;
    private String completionType;

    // timer
    @Size(max = 256)
    private String timerDuration;

    // outputFilter (опционально)
    private Object outputFilter;
}
