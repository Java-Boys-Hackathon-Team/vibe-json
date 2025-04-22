package ru.javaboys.vibejson.wfdefenition;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Map;

@Data
@EntityDescription("Конфигурация отправки в RabbitMQ")
public class SendToRabbitmqConfigDto {

    @Valid
    private RabbitmqConnectionRefDto connectionRef;

    @Valid
    private RabbitmqConnectionDefDto connectionDef;

    @NotBlank
    @Size(max = 255)
    private String exchange;

    @NotBlank
    @Size(max = 255)
    private String routingKey;

    @NotBlank
    @Size(max = 255)
    private String message;

    @Valid
    private Map<String, Object> messageProperties;
}

