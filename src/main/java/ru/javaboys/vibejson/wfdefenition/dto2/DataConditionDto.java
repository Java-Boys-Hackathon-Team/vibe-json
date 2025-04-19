package ru.javaboys.vibejson.wfdefenition.dto2;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
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
