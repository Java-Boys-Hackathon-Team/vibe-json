package ru.javaboys.vibejson.wfdefenition.dto2;

import java.util.Map;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WorkflowCallDto {

    // todo args

    // todo workflowRef

    @NotNull
    @Valid
    private WorkflowDefDto workflowDef;

    // Поведение при неудаче (пока как заглушка)
    @Valid
    private RetryConfigDto retryConfig;

    // Преобразование/фильтрация результата шага
    private Map<String, Object> outputFilter;

    // todo failActivityResul

}
