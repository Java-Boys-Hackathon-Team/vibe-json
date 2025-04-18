package ru.javaboys.vibejson.wfdefenition.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
public class SwitchDto {

    @NotBlank
    @Size(max = 255)
    private String id;

    @Size(max = 255)
    private String description;

    @NotBlank
    @Size(max = 255)
    private String type;

    @Valid
    @NotNull
    @Size(min = 1)
    private List<DataCondition> dataConditions;

    @Valid
    @NotNull
    private DefaultDataTransition defaultTransition;

    @Data
    public static class DataCondition {

        @NotBlank
        @Size(max = 400)
        private String condition;

        @Size(max = 400)
        private String conditionDescription;

        @Size(max = 400)
        private String transition;

    }

    @Data
    public static class DefaultDataTransition {

        @NotBlank
        @Size(max = 255)
        private String transition;

        @Size(max = 400)
        private String conditionDescription;

    }
}
