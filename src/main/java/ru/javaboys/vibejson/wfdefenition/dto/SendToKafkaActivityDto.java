package ru.javaboys.vibejson.wfdefenition.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.Map;
import java.util.UUID;

@Data
public class SendToKafkaActivityDto {

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
        private SendToKafkaConfig sendToKafkaConfig;
    }

    @Data
    public static class SendToKafkaConfig {

        @Valid
        private ConnectionRef connectionRef;

        @Valid
        private ConnectionDef connectionDef;

        @NotBlank
        @Size(max = 255)
        private String topic;

        @NotBlank
        @Size(max = 255)
        private String key;

        @Valid
        @NotNull
        private Message message;

        @NotNull
        private Map<String, Object> messageProperties;
    }

    @Data
    public static class ConnectionRef {

        @NotNull
        private UUID id;

        @NotBlank
        @Size(max = 255)
        private String name;

        private Integer version;

        @Size(max = 255)
        private String tenantId;
    }

    @Data
    public static class Message {

        @NotBlank
        @Size(max = 255)
        private String payload;
    }

    @Data
    public static class ConnectionDef {

        @NotBlank
        @Size(max = 255)
        private String bootstrapServers;

        @Valid
        private AuthDef authDef;
    }

    @Data
    public static class AuthDef {

        @NotBlank
        @Size(max = 255)
        private String type;

        @Valid
        private SaslAuth sasl;

        @Valid
        private TlsAuth tls;
    }

    @Data
    public static class SaslAuth {

        @NotBlank
        @Size(max = 255)
        private String protocol;

        @NotBlank
        @Size(max = 255)
        private String mechanism;

        @Size(max = 255)
        private String username;

        @Size(max = 255)
        private String password;

        @Valid
        private SslDef sslDef;

        @Size(max = 255)
        private String tokenUri;
    }

    @Data
    public static class SslDef {

        @Size(max = 255)
        private String trustStoreType;

        private String trustStoreCertificates;
    }

    @Data
    public static class TlsAuth {

        @NotBlank
        private String keyStoreCertificates;

        @NotBlank
        private String keyStoreKey;

        @NotBlank
        private String trustStoreCertificates;

        @NotBlank
        @Size(max = 255)
        private String trustStoreType;
    }
}
