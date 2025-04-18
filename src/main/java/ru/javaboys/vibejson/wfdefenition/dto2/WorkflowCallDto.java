package ru.javaboys.vibejson.wfdefenition.dto2;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

@Data
public class WorkflowCallDto {

    @NotNull
    @Valid
    private WorkflowDefDto workflowDef;

    // Поведение при неудаче (пока как заглушка)
    @Valid
    private RetryConfigDto retryConfig;

    // Преобразование/фильтрация результата шага
    private Map<String, Object> outputFilter;
}
