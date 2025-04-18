package ru.javaboys.vibejson.wfdefenition.dto;

import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SendToSapDto {

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

    @NotNull
    private WorkflowCall workflowCall;

    @Size(max = 255)
    private String outputFilter;

    @Data
    public static class WorkflowCall {
        @NotNull
        private Map<String, Object> args;

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
        private SendToSapConfig sendToSapConfig;

    }

    @Data
    public static class SendToSapConfig {

        @Valid
        private ConnectionRef connectionRef;

        @Valid
        private ConnectionDef connectionDef;

        @NotBlank
        @Size(max = 255)
        private String idoc;

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

        @Valid
        @NotNull
        private Props props;

    }

    @Data
    public static class Props {

        @NotBlank
        @Size(max = 255)
        @JsonProperty("jco.client.lang")
        private String lang;

        @NotBlank
        @Size(max = 255)
        @JsonProperty("jco.client.passwd")
        private String passwd;

        @NotBlank
        @Size(max = 255)
        @JsonProperty("jco.client.user")
        private String user;

        @NotNull
        @JsonProperty("jco.client.sysnr")
        private Integer sysnr;

        @NotNull
        @JsonProperty("jco.client.pool_capacity")
        private Integer poolCapacity;

        @NotNull
        @JsonProperty("jco.client.peak_limit")
        private Integer peakLimit;

        @NotNull
        @JsonProperty("jco.client.client")
        private Integer client;

        @NotBlank
        @Size(max = 255)
        @JsonProperty("jco.client.ashost")
        private String ashost;
    }

}
