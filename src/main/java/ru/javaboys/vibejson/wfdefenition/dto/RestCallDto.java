package ru.javaboys.vibejson.wfdefenition.dto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpMethod;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RestCallDto {

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
        private RetryConfig retryConfig;

        @Valid
        @NotNull
        private SimpleWorkflowDefinition workflowDef;
    }

    @Data
    public static class RetryConfig {
        @Size(max = 256)
        private String initialInterval;

        @Size(max = 256)
        private String maxInterval;

        @Min(1)
        private Integer maxAttempts;

        @DecimalMin("1.0")
        private Float backoffCoefficient;
    }

    @Data
    public static class SimpleWorkflowDefinition {
        @NotBlank
        @Size(max = 255)
        private String type;

        @Valid
        @NotNull
        private RestCallDetails details;
    }

    @Data
    public static class RestCallDetails {
        private Map<String, Object> inputValidateSchema;
        private Map<String, Object> outputValidateSchema;

        @Valid
        @NotNull
        private RestCallConfig restCallConfig;
    }

    @Data
    public static class RestCallConfig {
        @Valid
        private List<Predicate> resultHandlers;

        @Valid
        private RestCallTemplateRef restCallTemplateRef;

        @Valid
        private RestCallTemplateDef restCallTemplateDef;

    }

    @Data
    public static class Predicate {

        private Integer respCode;

        @Valid
        private List<Integer> respCodes;

        @Valid
        private RespCodeInterval respCodeInterval;

        @Valid
        private List<PathValueValidation> respValueAnyOf;
    }

    @Data
    public static class RespCodeInterval {
        private Integer start;
        private Integer end;
    }

    @Data
    public static class PathValueValidation {
        @NotBlank
        @Size(max = 255)
        private String jsonPath;

        @Valid
        @NotEmpty
        private List<Object> values;

        @Valid
        private PathValueValidation and;
    }

    @Data
    public static class RestCallTemplateRef {

        @NotNull
        @Size(max = 255)
        private UUID id;

        @NotBlank
        @Size(max = 255)
        private String name;

        @Size(max = 255)
        private Integer version;

        @NotBlank
        @Size(max = 255)
        private String tenantId;
    }

    @Data
    public static class RestCallTemplateDef {

        @NotNull
        private HttpMethod method;

        @Size(max = 255)
        private String url;

        @Size(max = 255)
        private String bodyTemplate;

        @Valid
        @NotNull
        private Headers headers;

        @Size(max = 255)
        private String curl;

        @Valid
        @NotNull
        private AuthDef authDef;
    }

    @Data
    public static class Headers {

        @NotBlank
        @Size(max = 255)
        private String headers;
    }

    @Data
    public static class AuthDef {

        @NotBlank
        @Size(max = 255)
        private String type;

        @Valid
        private BasicAuth basic;

        @Valid
        private OAuth2Auth oauth2;
    }

    @Data
    public static class BasicAuth {

        @NotBlank
        @Size(max = 255)
        private String login;

        @NotBlank
        @Size(max = 255)
        private String password;
    }

    @Data
    public static class OAuth2Auth {
        @NotBlank
        @Size(max = 255)
        private String issuerLocation;

        @NotBlank
        @Size(max = 255)
        private String clientId;

        @NotBlank
        @Size(max = 255)
        private String clientSecret;

        @NotBlank
        @Size(max = 255)
        private String grantType;
    }
}
