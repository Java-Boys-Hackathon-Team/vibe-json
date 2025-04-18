package ru.javaboys.vibejson.wfdefenition.dto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SendToRabbitMqDto {

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
        private SendToRabbitMqConfig details;
    }

    @Data
    public static class SendToRabbitMqConfig {

        @Valid
        private ConnectionRef connectionRef;

        @Valid
        private ConnectionDef connectionDef;

        @NotBlank
        @Size(max = 255)
        private String exchange;

        @NotBlank
        @Size(max = 255)
        private String routingKey;

        @NotBlank
        @Size(max = 255)
        private String message;

        @NotNull
        private Map<String, Object> messageProperties;
    }

    @Data
    public static class ConnectionRef {

        private UUID id;

        @Size(max = 255)
        private String name;

        @Min(1)
        private Integer version;

        @Size(max = 255)
        private String tenantId;
    }

    @Data
    public static class ConnectionDef {

        @NotBlank
        @Size(max = 255)
        private String userName;

        @NotBlank
        @Size(max = 255)
        private String userPass;

        @Size(min = 1)
        private List<String> addresses;

        @NotBlank
        @Size(max = 255)
        private String virtualHost;
    }
}
