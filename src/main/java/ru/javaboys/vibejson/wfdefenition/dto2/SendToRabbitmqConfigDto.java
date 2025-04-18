package ru.javaboys.vibejson.wfdefenition.dto2;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SendToRabbitmqConfigDto {

    @NotNull
    @Valid
    private RabbitmqConnectionDefDto connectionDef;

    @NotBlank
    private String exchange;

    @NotBlank
    private String routingKey;

    @NotBlank
    private String message;

    @Valid
    private RabbitmqMessagePropertiesDto messageProperties;
}

