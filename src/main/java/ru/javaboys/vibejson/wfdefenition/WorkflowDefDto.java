package ru.javaboys.vibejson.wfdefenition;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@EntityDescription("Определение рабочего процесса")
public class WorkflowDefDto {

    @NotBlank
    private String type; // например: rest_call, send_to_kafka, transform и т.д.

    @Valid
    private WorkflowDetailsDto details;
}
