package ru.javaboys.vibejson.wfdefenition.dto;

import java.util.Map;
import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class XsltTransformDto {

    @NotBlank
    @Size(max = 255)
    private String id;

    @Size(max = 255)
    private String description;

    @NotBlank
    @Size(max = 255)
    private String transition;

    @Valid
    @NotNull
    private WorkflowCall workflowCall;

    @Size(max = 255)
    private String outputFilter;

    @Data
    public static class WorkflowCall {

        @NotNull
        private Map<String, Object> input;

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
        private XsltTransformConfig xsltTransformConfig;
    }

    @Data
    public static class XsltTransformConfig {

        @Valid
        @NotNull
        private XsltTemplateRef xsltTemplateRef;

        @Valid
        @NotNull
        private XsltTransformTargetRef xsltTransformTargetRef;

        @Size(max = 255)
        private String xsltTemplate;

        @Size(max = 255)
        private String xsltTransformTarget;

    }

    @Data
    public static class XsltTemplateRef {

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
    public static class XsltTransformTargetRef {

        private UUID id;

        @NotBlank
        @Size(max = 255)
        private String name;

        private Integer version;

        @Size(max = 255)
        private String tenantId;
    }

}