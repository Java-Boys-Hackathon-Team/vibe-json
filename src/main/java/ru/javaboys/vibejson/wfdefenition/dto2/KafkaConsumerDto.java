package ru.javaboys.vibejson.wfdefenition.dto2;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

@Data
public class KafkaConsumerDto {

    @NotBlank
    private String topic;

    @NotNull
    @Valid
    private KafkaConnectionDefDto connectionDef;

    private Map<String, Object> payloadValidateSchema;

    private Map<String, Object> keyValidateSchema;

    private Map<String, Object> headersValidateSchema;

    private Map<String, Object> outputTemplate;
}