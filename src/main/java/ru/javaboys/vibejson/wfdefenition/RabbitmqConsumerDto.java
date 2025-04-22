package ru.javaboys.vibejson.wfdefenition;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Map;

@Data
@EntityDescription("Потребитель RabbitMQ")
public class RabbitmqConsumerDto {

    @NotBlank
    @Size(max = 255)
    private String queue;

    @NotNull
    @Valid
    private RabbitmqConnectionDefDto connectionDef;

    private Map<String, Object> payloadValidateSchema;

    private Map<String, Object> outputTemplate;
}
