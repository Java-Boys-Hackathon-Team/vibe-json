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
public class SendToS3Dto {

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
    @Valid
    private WorkflowCall workflowCall;

    @Size(max = 255)
    private String outputFilter;

    @Data
    public static class WorkflowCall {
        private Map<String, Object> args;

        @NotNull
        @Valid
        private SimpleWorkflowDefinition workflowDef;

    }

    @Data
    public static class SimpleWorkflowDefinition {

        @NotBlank
        @Size(max = 255)
        private String type;

        @NotNull
        @Valid
        private Details details;

    }

    @Data
    public static class Details {

        @NotNull
        @Valid
        private SendToS3Config sendToS3Config;

    }

    @Data
    public static class SendToS3Config {

        @Valid
        private ConnectionRef connectionRef;

        @Valid
        private ConnectionDef connectionDef;

        @NotBlank
        @Size(max = 255)
        private String bucket;

        @NotBlank
        @Size(max = 255)
        private String region;

        @NotNull
        @Valid
        private S3File s3File;

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
        private String endpoint;

        @NotNull
        @Valid
        private AuthDef authDef;

    }

    @Data
    public static class AuthDef {

        @NotBlank
        @Size(max = 255)
        private String type;

        @NotNull
        @Valid
        private KeyAuth accessKeyAuth;

    }

    @Data
    public static class KeyAuth {

        @NotBlank
        @Size(max = 255)
        private String accessKey;

        @NotBlank
        @Size(max = 255)
        private String secretKey;

    }

    @Data
    public static class S3File {

        @NotBlank
        @Size(max = 255)
        private String filePath;

        @NotBlank
        @Size(max = 255)
        private String content;

    }

}
