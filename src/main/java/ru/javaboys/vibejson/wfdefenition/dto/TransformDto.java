package ru.javaboys.vibejson.wfdefenition.dto;

import java.util.Map;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TransformDto {

    @NotBlank
    @Size(max = 255)
    private String id;

    @Size(max = 255)
    private String description;

    @NotBlank
    @Size(max = 255)
    private String transition;

    @NotBlank
    @Size(max = 255)
    private String type;

    @Valid
    @NotNull
    private WorkflowCall workflowCall;

    @Size(max = 255)
    private String outputFilter;

    @Data
    public static class WorkflowCall {

        @NotNull
        private Map<String, Object> args;

        @Valid
        @NotNull
        private WorkflowDef workflowDef;
    }

    @Data
    public static class WorkflowDef {

        @NotBlank
        @Size(max = 255)
        private String type;

        @Valid
        @NotNull
        private Details details;
    }

    @Data
    public static class Details {

        @Valid
        @NotNull
        private TransformConfig transformConfig;
    }

    @Data
    public static class TransformConfig {

        @NotBlank
        @Size(max = 255)
        private String type;

        @NotNull
        private Map<String, Object> target;
    }

}
