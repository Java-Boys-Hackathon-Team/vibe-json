package ru.javaboys.vibejson.wfdefenition;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

@Data
@EntityDescription("Вызов рабочего процесса")
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
