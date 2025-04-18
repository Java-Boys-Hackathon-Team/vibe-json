package ru.javaboys.vibejson.wfdefenition.dto2;


import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WorkflowDefinitionDto {

    @NotBlank
    @Size(max = 255)
    private String type; // чаще всего "complex"

    @NotBlank
    @Size(max = 255)
    private String name;

    @Size(max = 4000)
    private String description;

    private Integer version = 1; // по умолчанию

    private String tenantId = "default"; // по умолчанию

    @NotNull
    private DetailsDto details;

    private CompiledDto compiled;

    private FlowEditorConfigDto flowEditorConfig;
}
