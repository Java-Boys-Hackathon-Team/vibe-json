package ru.javaboys.vibejson.wfdefenition.dto;

import java.util.Map;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AwaitForMessageDto {

    @NotBlank
    @Size(max = 255)
    private String id;

    @Size(max = 255)
    private String description;

    @Size(max = 255)
    private String transition;

    @NotBlank
    @Size(max = 255)
    private String type;

    @NotNull
    private WorkflowCall workflowCall;

    @Data
    public static class WorkflowCall {

        private Map<String, Object> args;

        @NotNull
        private SimpleWorkflowDefinition workflowDef;
    }

    @Data
    public static class SimpleWorkflowDefinition {

        @NotBlank
        @Size(max = 255)
        private String type;

        @NotNull
        private Details details;
    }

    @Data
    public static class Details {

        @NotNull
        private AwaitForMessageConfig awaitForMessageConfig;
    }

    @Data
    public static class AwaitForMessageConfig {

        @NotBlank
        @Size(max = 255)
        private String messageName;
    }

}
