package ru.javaboys.vibejson.wfdefenition.dto;

import java.util.Map;
import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DbCallDto {

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
        private Map<String, Object> args;

        @Valid
        @NotNull
        private SimpleWorkflowDefinition workflowDef;
    }

    @Data
    public static class SimpleWorkflowDefinition {

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
        private DatabaseCallConfig databaseCallConfig;
    }

    @Data
    public static class DatabaseCallConfig {

        @Valid
        private DatabaseCallRef databaseCallRef;

        @Valid
        private DatabaseCallDef databaseCallDef;

        @Size(max = 255)
        private String dataSourceId;

        @Valid
        private DataSourceDef dataSourceDef;
    }

    @Data
    public static class DatabaseCallRef {

        private UUID id;

        @Size(max = 255)
        private String name;

        @Min(1)
        private Integer version;

        @Size(max = 255)
        private String tenantId;
    }

    @Data
    public static class DatabaseCallDef {

        @NotBlank
        @Size(max = 255)
        private String type;

        private String sql;

        @Size(max = 255)
        private String schema;

        @Size(max = 255)
        private String catalog;

        @Size(max = 255)
        private String functionName;

        private Map<String, String> inParameters;

        private Map<String, String> outParameter;
    }

    @Data
    public static class DataSourceDef {

        @NotBlank
        @Size(max = 255)
        private String url;

        @NotBlank
        @Size(max = 255)
        private String className;

        @NotBlank
        @Size(max = 255)
        private String userName;

        @NotBlank
        @Size(max = 255)
        private String userPass;
    }

}
