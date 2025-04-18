package ru.javaboys.vibejson.wfdefenition.dto2;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DataConditionDto {

    @NotBlank
    private String transition;

    @NotBlank
    private String condition;

    private String conditionDescription;
}
