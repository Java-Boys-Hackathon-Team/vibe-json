package ru.javaboys.vibejson.wfdefenition;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@EntityDescription("Состояние данных")
public class DataConditionDto {

    @NotBlank
    @Size(max = 400)
    private String transition;

    @NotBlank
    @Size(max = 400)
    private String condition;

    @Size(max = 400)
    private String conditionDescription;
}
