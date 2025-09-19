package ru.javaboys.vibejson.wfdefenition.root;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.javaboys.vibejson.wfdefenition.CompiledDto;
import ru.javaboys.vibejson.wfdefenition.DetailsDto;
import ru.javaboys.vibejson.wfdefenition.EntityDescription;

@Data
@EntityDescription("Определение рабочего процесса")
@JsonIgnoreProperties(value = {"flowEditorConfig"})
public class WorkflowDefinitionDto {

    @NotBlank
    @Size(max = 255)
    private String type = "complex";

    @NotBlank
    @Size(max = 255)
    private String name;

    @Size(max = 4000)
    private String description;

    private Integer version = 1;

    private String tenantId = "default";

    @NotNull
    @Valid
    private DetailsDto details;

    @Valid
    private CompiledDto compiled;
}
