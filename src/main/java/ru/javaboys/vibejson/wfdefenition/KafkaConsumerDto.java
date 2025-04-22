package ru.javaboys.vibejson.wfdefenition;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Map;

@Data
@EntityDescription("Потребитель Kafka")
public class KafkaConsumerDto {

    @NotBlank
    @Size(max = 255)
    private String topic;

    @NotNull
    @Valid
    private KafkaConnectionDefDto connectionDef;

    private Map<String, Object> payloadValidateSchema;

    private Map<String, Object> keyValidateSchema;

    private Map<String, Object> headersValidateSchema;

    private Map<String, Object> outputTemplate;
}